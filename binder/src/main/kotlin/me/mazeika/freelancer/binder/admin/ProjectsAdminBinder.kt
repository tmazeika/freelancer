package me.mazeika.freelancer.binder.admin

import javafx.beans.binding.Bindings
import javafx.beans.property.*
import javafx.beans.value.ObservableBooleanValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import me.mazeika.freelancer.binder.services.DialogService
import me.mazeika.freelancer.model.Client
import me.mazeika.freelancer.model.Project
import me.mazeika.freelancer.model.Store
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

class ProjectsAdminBinder @Inject constructor(
    private val store: Store,
    dialogService: DialogService,
) : EntityAdminBinder<ProjectsAdminBinder.ProjectBinder, ProjectsAdminBinder.FilledProjectBinder>(
    entityName = "Project",
    dialogService
) {
    private val clientNames: ObservableList<String> =
        FXCollections.observableArrayList()

    init {
        store.onClientsUpdated += {
            clientNames.setAll(store.getClients().map(Client::name))
        }
        store.onProjectsUpdated += {
            entities.setAll(store.getProjects().map(::FilledProjectBinder))
        }
    }

    override fun createEmptyBinder(): ProjectBinder = EmptyProjectBinder()

    override fun copyFilledBinder(binder: FilledProjectBinder): FilledProjectBinder =
        FilledProjectBinder(binder.project)

    override fun create(binder: ProjectBinder) {
        store.addProject(binder.createProject())
    }

    override fun edit(binder: FilledProjectBinder) {
        store.replaceProject(
            old = binder.project,
            new = binder.createProject()
        )
    }

    override fun delete(binder: FilledProjectBinder) {
        store.removeProject(binder.project)
    }

    abstract inner class ProjectBinder : EntityBinder {
        val clientNames: ObservableList<String> =
            FXCollections.unmodifiableObservableList(this@ProjectsAdminBinder.clientNames)
        val currencies: List<String> =
            Currency.getAvailableCurrencies().map { it.currencyCode }.sorted()
        val maxNameLength: Int = 128

        abstract val clientName: StringProperty
        abstract val name: StringProperty
        abstract val colorIndex: IntegerProperty
        abstract val hourlyRate: Property<BigDecimal>
        abstract val currency: StringProperty

        internal fun createProject(): Project = Project(
            client = store.getClient(clientName.value),
            name = name.value,
            colorIndex = colorIndex.value,
            hourlyRate = hourlyRate.value,
            currency = Currency.getInstance(currency.value)
        )
    }

    private inner class EmptyProjectBinder : ProjectBinder() {
        override val clientName: StringProperty = SimpleStringProperty("")
        override val name: StringProperty = SimpleStringProperty("")
        override val colorIndex: IntegerProperty = SimpleIntegerProperty(0)
        override val hourlyRate: Property<BigDecimal> =
            SimpleObjectProperty(BigDecimal.ZERO)
        override val currency: StringProperty =
            SimpleStringProperty(Currency.getInstance(Locale.getDefault()).currencyCode)

        override val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
                val clientName = clientName.value
                val name = name.value
                val clientExists = store.containsClient(clientName)
                val unique = !store.containsProject(clientName, name)
                val validLength = name.length in 1..maxNameLength
                clientExists && unique && validLength
            }, clientName, name)

        override fun toString(): String = name.value
    }

    inner class FilledProjectBinder(internal val project: Project) :
        ProjectBinder() {
        override val clientName: StringProperty =
            SimpleStringProperty(project.client.name)
        override val name: StringProperty = SimpleStringProperty(project.name)
        override val colorIndex: IntegerProperty =
            SimpleIntegerProperty(project.colorIndex)
        override val hourlyRate: Property<BigDecimal> =
            SimpleObjectProperty(project.hourlyRate)
        override val currency: StringProperty =
            SimpleStringProperty(project.currency.currencyCode)

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
