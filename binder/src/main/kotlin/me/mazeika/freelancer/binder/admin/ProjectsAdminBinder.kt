package me.mazeika.freelancer.binder.admin

import javafx.beans.binding.Bindings
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableBooleanValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Node
import me.mazeika.freelancer.binder.i18n.I18nService
import me.mazeika.freelancer.binder.services.DialogService
import me.mazeika.freelancer.model.Client
import me.mazeika.freelancer.model.Project
import me.mazeika.freelancer.model.Store
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

class ProjectsAdminBinder @Inject constructor(
    private val store: Store,
    private val dialogService: DialogService,
    private val i18nService: I18nService
) : AdminBinder<ProjectsAdminBinder.ProjectBinder, ProjectsAdminBinder.FilledProjectBinder>() {

    private val _clientNames: ObservableList<String> =
        FXCollections.observableArrayList()

    init {
        store.onClientsUpdated += {
            _clientNames.setAll(store.getClients().map(Client::name))
        }
        store.onProjectsUpdated += {
            entities.setAll(store.getProjects().map(::FilledProjectBinder))
        }
    }

    override fun onCreate(dialogViewFactory: (ProjectBinder) -> Node): Boolean {
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

    override fun onEdit(dialogViewFactory: (ProjectBinder) -> Node): Boolean {
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
            message = "Are you sure you want to delete \"$binder\"?"
        )
        if (ok) {
            store.removeProject(binder.project)
        }
        return ok
    }

    abstract inner class ProjectBinder(
        clientName: String,
        name: String,
        colorIndex: Int,
        hourlyRate: BigDecimal,
        currency: Currency
    ) {

        val clientNames: ObservableList<String> =
            FXCollections.unmodifiableObservableList(_clientNames)
        val maxNameLength: Int = 128

        val clientName: StringProperty = SimpleStringProperty(clientName)
        val name: StringProperty = SimpleStringProperty(name)
        val colorIndex: ObjectProperty<Int> = SimpleObjectProperty(colorIndex)
        val hourlyRate: ObjectProperty<BigDecimal> =
            SimpleObjectProperty(hourlyRate)
        val currency: ObjectProperty<Currency> = SimpleObjectProperty(currency)

        internal fun createProject(): Project = Project(
            client = store.getClient(clientName.value),
            name = name.value.trim(),
            colorIndex = colorIndex.value,
            hourlyRate = hourlyRate.value,
            currency = currency.value
        )
    }

    private inner class EmptyProjectBinder : ProjectBinder(
        clientName = "",
        name = "",
        colorIndex = 0,
        hourlyRate = BigDecimal.ZERO,
        currency = i18nService.defaultCurrency
    ) {

        val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
                val clientName = clientName.value
                val name = name.value.trim()
                val isProjectUnique = !store.containsProject(clientName, name)
                val isNameValid = name.isNotEmpty()
                isProjectUnique && isNameValid
            }, clientName, name)

        override fun toString(): String = name.value
    }

    inner class FilledProjectBinder(internal val project: Project) :
        ProjectBinder(
            clientName = project.client.name,
            name = project.name,
            colorIndex = project.colorIndex,
            hourlyRate = project.hourlyRate,
            currency = project.currency
        ) {

        val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
                val clientName = clientName.value
                val name = name.value.trim()
                val isUnchanged = project.isIdentifiedBy(clientName, name)
                val isProjectUnique = !store.containsProject(clientName, name)
                val isNameValid = name.isNotEmpty()
                (isUnchanged || isProjectUnique) && isNameValid
            }, clientName, name)

        override fun toString(): String = project.name
    }
}
