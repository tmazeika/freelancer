package me.mazeika.freelancer.binder.admin

import javafx.beans.binding.Bindings
import javafx.beans.value.ObservableBooleanValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Node
import me.mazeika.freelancer.binder.i18n.I18nService
import me.mazeika.freelancer.binder.services.DialogService
import me.mazeika.freelancer.model.Project
import me.mazeika.freelancer.model.Store
import java.math.BigDecimal
import javax.inject.Inject

class ProjectsAdminBinder @Inject constructor(
    private val store: Store,
    private val dialogService: DialogService,
    private val i18nService: I18nService
) : AdminBinder<MutableProjectBinder, SnapshotProjectBinder>() {

    val allClients: ObservableList<SnapshotClientBinder> =
        FXCollections.observableArrayList()

    init {
        store.onClientsUpdated += {
            allClients.setAll(store.getClients().map(::SnapshotClientBinder))
            isCreateVisible.value = store.getClients().isNotEmpty()
        }
        store.onProjectsUpdated += {
            items.setAll(store.getProjects().map(::SnapshotProjectBinder))
        }
    }

    override fun onCreate(dialogViewFactory: (MutableProjectBinder) -> Node): Boolean {
        val binder = EmptyProjectBinder()
        val ok = dialogService.prompt(
            title = "Create Project",
            content = dialogViewFactory(binder),
            isValid = binder.isValid
        )
        if (ok) {
            store.addProject(binder.createProject())
        }
        return ok
    }

    override fun onEdit(dialogViewFactory: (MutableProjectBinder) -> Node): Boolean {
        val binder = FilledProjectBinder(selected.value.project)
        val ok = dialogService.prompt(
            title = "Edit Client",
            content = dialogViewFactory(binder),
            isValid = binder.isValid
        )
        if (ok) {
            store.replaceProject(
                old = binder.project,
                new = binder.createProject()
            )
        }
        return ok
    }

    override fun onDelete(): Boolean {
        val binder = FilledProjectBinder(selected.value.project)
        val ok = dialogService.confirm(
            title = "Delete Client",
            message = "Are you sure you want to delete \"${binder.name.value}\"?"
        )
        if (ok) {
            store.removeProject(binder.project)
        }
        return ok
    }

    private inner class EmptyProjectBinder : MutableProjectBinder(
        client = SnapshotClientBinder(store.getClients()[0]),
        name = "",
        colorIndex = 0,
        hourlyRate = BigDecimal.ZERO,
        currency = i18nService.defaultCurrency
    ) {

        val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
                val client = client.value
                val name = name.value.trim()
                val isProjectUnique =
                    !store.containsProject(client.name.value, name)
                val isNameValid = name.isNotEmpty()
                isProjectUnique && isNameValid
            }, client, name)

        override fun toString(): String = name.value
    }

    private inner class FilledProjectBinder(val project: Project) :
        MutableProjectBinder(
            client = SnapshotClientBinder(project.client),
            name = project.name,
            colorIndex = project.colorIndex,
            hourlyRate = project.hourlyRate,
            currency = project.currency
        ) {

        val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
                val client = client.value
                val name = name.value.trim()
                val isUnchanged =
                    project.isIdentifiedBy(client.name.value, name)
                val isProjectUnique =
                    !store.containsProject(client.name.value, name)
                val isNameValid = name.isNotEmpty()
                (isUnchanged || isProjectUnique) && isNameValid
            }, client, name)

        override fun toString(): String = project.name
    }
}
