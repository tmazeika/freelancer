package me.mazeika.freelancer.binder.admin

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableBooleanValue
import me.mazeika.freelancer.binder.services.DialogService
import me.mazeika.freelancer.model.Client
import me.mazeika.freelancer.model.Project
import me.mazeika.freelancer.model.Store
import java.util.*
import javax.inject.Inject

class ProjectsAdminBinder @Inject constructor(
    private val store: Store,
    dialogService: DialogService,
) : EntityAdminBinder<ProjectsAdminBinder.ProjectBinder, ProjectsAdminBinder.FilledProjectBinder>(
    entityName = "Project",
    dialogService
) {
    init {
        store.onProjectsUpdated += ::updateProjects
        updateProjects()
    }

    override fun createEmptyBinder(): ProjectBinder = EmptyProjectBinder()

    override fun create(binder: ProjectBinder) {
        val client = store.getClient(binder.clientName.value)
        store.addProject(
            Project(
                client,
                binder.name.value,
                0,
                0,
                Currency.getInstance("USD")
            )
        )
        updateProjects()
    }

    override fun edit(binder: FilledProjectBinder) {
        val client = store.getClient(binder.clientName.value)
        store.replaceProject(
            old = binder.project,
            new = Project(
                client,
                binder.name.value,
                0,
                0,
                Currency.getInstance("USD")
            )
        )
        updateProjects()
    }

    override fun delete(binder: FilledProjectBinder) {
        store.removeProject(binder.project)
        updateProjects()
    }

    private fun updateProjects() {
        entities.setAll(store.getProjects().map(::FilledProjectBinder))
    }

    abstract inner class ProjectBinder : EntityBinder {
        val clientNames: List<String>
            get() = store.getClients().map(Client::name)
        abstract val clientName: StringProperty
        abstract val name: StringProperty
        val maxNameLength: Int = 128
    }

    private inner class EmptyProjectBinder : ProjectBinder() {
        override val clientName: StringProperty = SimpleStringProperty("")
        override val name: StringProperty = SimpleStringProperty("")

        override val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
                val clientName = clientName.value
                val name = name.value
                val unique = !store.containsProject(clientName, name)
                val validLength = name.length in 1..maxNameLength
                unique && validLength
            }, clientName, name)

        override fun toString(): String = name.value
    }

    inner class FilledProjectBinder(internal val project: Project) :
        ProjectBinder() {
        override val clientName: StringProperty =
            SimpleStringProperty(project.client.name)
        override val name: StringProperty = SimpleStringProperty(project.name)

        override val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
                val clientName = clientName.value
                val name = name.value
                val unchanged = project.isIdentifiedBy(clientName, name)
                val unique = !store.containsProject(clientName, name)
                val validLength = name.length in 1..maxNameLength
                (unchanged || unique) && validLength
            }, clientName, name)

        override fun toString(): String = project.name
    }
}
