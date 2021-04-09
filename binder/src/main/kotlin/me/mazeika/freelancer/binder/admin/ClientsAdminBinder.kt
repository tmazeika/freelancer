package me.mazeika.freelancer.binder.admin

import javafx.beans.binding.Bindings
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableBooleanValue
import javafx.scene.Node
import me.mazeika.freelancer.binder.i18n.I18nService
import me.mazeika.freelancer.binder.services.DialogService
import me.mazeika.freelancer.model.Client
import me.mazeika.freelancer.model.Store
import java.util.*
import javax.inject.Inject

class ClientsAdminBinder @Inject constructor(
    private val store: Store,
    private val dialogService: DialogService,
    private val i18nService: I18nService
) : AdminBinder<ClientsAdminBinder.ClientBinder, ClientsAdminBinder.FilledClientBinder>() {

    init {
        store.onClientsUpdated += {
            entities.setAll(store.getClients().map(::FilledClientBinder))
        }
    }

    override fun onCreate(dialogViewFactory: (ClientBinder) -> Node): Boolean {
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

    override fun onEdit(dialogViewFactory: (ClientBinder) -> Node): Boolean {
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
            message = "Are you sure you want to delete \"$binder\"? " +
                    "This will also delete all of the client's projects."
        )
        if (ok) {
            store.removeClient(binder.client)
        }
        return ok
    }

    abstract class ClientBinder(name: String, currency: Currency) {

        val name: StringProperty = SimpleStringProperty(name)
        val currency: ObjectProperty<Currency> = SimpleObjectProperty(currency)
        val maxNameLength: Int = 128

        internal fun createClient(): Client =
            Client(name = name.value.trim(), currency = currency.value)
    }

    private inner class EmptyClientBinder :
        ClientBinder(name = "", currency = i18nService.defaultCurrency) {

        val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
                val name = name.value.trim()
                val isUnique = !store.containsClient(name)
                val isNameValid = name.isNotEmpty()
                isUnique && isNameValid
            }, name)

        override fun toString(): String = name.value
    }

    inner class FilledClientBinder(internal val client: Client) :
        ClientBinder(name = client.name, currency = client.currency) {

        val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
                val name = name.value.trim()
                val isUnchanged = client.isIdentifiedBy(name)
                val isUnique = !store.containsClient(name)
                val isNameValid = name.isNotEmpty()
                (isUnchanged || isUnique) && isNameValid
            }, name)

        override fun toString(): String = client.name
    }
}
