package me.mazeika.freelancer.binder.admin

import javafx.beans.binding.Bindings
import javafx.beans.value.ObservableBooleanValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Node
import me.mazeika.freelancer.binder.i18n.I18nService
import me.mazeika.freelancer.binder.services.DialogService
import me.mazeika.freelancer.binder.util.bindContent
import me.mazeika.freelancer.binder.util.isNotEmpty
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
) : AdminBinder<ProjectBinder, ProjectSnapshot>() {

    val allClients: ObservableList<ClientSnapshot> =
        FXCollections.observableArrayList()

    init {
        allClients.bindContent(store.clients, ::ClientSnapshot)
        items.bindContent(store.projects, ::ProjectSnapshot)
        isCreateVisible.bind(allClients.isNotEmpty())

        // TODO: remove seed
        store.addProject(
            Project(
                Client(
                    "John Doe PSC Ltd",
                    Currency.getInstance("GBP")
                ),
                "ACME Website",
                0,
                BigDecimal(56),
                Currency.getInstance("USD")
            )
        )
        store.addProject(
            Project(
                Client(
                    "Mazeika LLC",
                    Currency.getInstance("USD")
                ), "freelancer", 1, BigDecimal(5), Currency.getInstance("USD")
            )
        )
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
            message = "Are you sure you want to delete \"${binder.name.value}\"?"
        )
        if (ok) {
            store.removeProject(binder.project)
        }
        return ok
    }

    private inner class EmptyProjectBinder : ProjectBinder(
        client = allClients[0],
        name = "",
        colorIndex = 0,
        hourlyRate = BigDecimal.ZERO,
        currency = i18nService.defaultCurrency
    ) {
        val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
                val client = client.value
                val name = name.value.trim()
                val isProjectUnique = !store.containsProject(client.name, name)
                val isNameValid = name.isNotEmpty()
                isProjectUnique && isNameValid
            }, client, name)
    }

    private inner class FilledProjectBinder(val project: Project) :
        ProjectBinder(
            client = ClientSnapshot(project.client),
            name = project.name,
            colorIndex = project.colorIndex,
            hourlyRate = project.hourlyRate,
            currency = project.currency
        ) {
        val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
                val client = client.value
                val name = name.value.trim()
                val isUnchanged = project.isIdentifiedBy(client.name, name)
                val isProjectUnique = !store.containsProject(client.name, name)
                val isNameValid = name.isNotEmpty()
                (isUnchanged || isProjectUnique) && isNameValid
            }, client, name)
    }
}
