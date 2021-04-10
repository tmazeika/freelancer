package me.mazeika.freelancer.binder.admin

import javafx.beans.binding.Bindings
import javafx.beans.value.ObservableBooleanValue
import javafx.scene.Node
import me.mazeika.freelancer.binder.i18n.I18nService
import me.mazeika.freelancer.binder.services.DialogService
import me.mazeika.freelancer.model.Client
import me.mazeika.freelancer.model.Store
import javax.inject.Inject

class ClientsAdminBinder @Inject constructor(
    private val store: Store,
    private val dialogService: DialogService,
    private val i18nService: I18nService
) : AdminBinder<MutableClientBinder, ClientsAdminBinder.FilledClientBinder>() {

    init {
        store.onClientsUpdated += {
            items.setAll(store.getClients().map(::FilledClientBinder))
        }
    }

    override fun onCreate(dialogViewFactory: (MutableClientBinder) -> Node): Boolean {
        val binder = EmptyClientBinder()
        val ok = dialogService.prompt(
            title = "Create Client",
            content = dialogViewFactory(binder),
            isValid = binder.isValid
        )
        if (ok) {
            store.addClient(binder.createClient())
        }
        return ok
    }

    override fun onEdit(dialogViewFactory: (MutableClientBinder) -> Node): Boolean {
        val binder = FilledClientBinder(selected.value.client)
        val ok = dialogService.prompt(
            title = "Edit Client",
            content = dialogViewFactory(binder),
            isValid = binder.isValid
        )
        if (ok) {
            store.replaceClient(
                old = binder.client,
                new = binder.createClient()
            )
        }
        return ok
    }

    override fun onDelete(): Boolean {
        val binder = FilledClientBinder(selected.value.client)
        val ok = dialogService.confirm(
            title = "Delete Client",
            message = "Are you sure you want to delete \"${binder.name}\"? " +
                    "This will also delete all of the client's projects."
        )
        if (ok) {
            store.removeClient(binder.client)
        }
        return ok
    }

    private inner class EmptyClientBinder :
        MutableClientBinder(
            name = "",
            currency = i18nService.defaultCurrency
        ) {

        val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
                val name = name.value.trim()
                val isUnique = !store.containsClient(name)
                val isNameValid = name.isNotEmpty()
                isUnique && isNameValid
            }, name)
    }

    inner class FilledClientBinder(internal val client: Client) :
        MutableClientBinder(name = client.name, currency = client.currency) {

        val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
                val name = name.value.trim()
                val isUnchanged = client.isIdentifiedBy(name)
                val isUnique = !store.containsClient(name)
                val isNameValid = name.isNotEmpty()
                (isUnchanged || isUnique) && isNameValid
            }, name)
    }
}
