package me.mazeika.freelancer.binder.admin

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableBooleanValue
import me.mazeika.freelancer.binder.services.DialogService
import me.mazeika.freelancer.model.Client
import me.mazeika.freelancer.model.Store
import java.util.*
import javax.inject.Inject

class ClientsAdminBinder @Inject constructor(
    private val store: Store,
    dialogService: DialogService,
) : EntityAdminBinder<ClientsAdminBinder.ClientBinder, ClientsAdminBinder.FilledClientBinder>(
    entityName = "Client",
    dialogService
) {
    init {
        store.onClientsUpdated += {
            entities.setAll(store.getClients().map(::FilledClientBinder))
        }
    }

    override fun createEmptyBinder(): ClientBinder = EmptyClientBinder()

    override fun copyFilledBinder(binder: FilledClientBinder): FilledClientBinder =
        FilledClientBinder(binder.client)

    override fun create(binder: ClientBinder) {
        store.addClient(binder.createClient())
    }

    override fun edit(binder: FilledClientBinder) {
        store.replaceClient(
            old = binder.client,
            new = binder.createClient()
        )
    }

    override fun delete(binder: FilledClientBinder) {
        store.removeClient(binder.client)
    }

    override val extraDeleteMessage: String =
        "This will also delete all of the client's projects."

    abstract class ClientBinder : EntityBinder {
        val currencies: List<String> =
            Currency.getAvailableCurrencies().map { it.currencyCode }.sorted()

        abstract val name: StringProperty
        abstract val currency: StringProperty
        val maxNameLength: Int = 128

        internal fun createClient(): Client = Client(name.value, Currency.getInstance(currency.value))
    }

    private inner class EmptyClientBinder : ClientBinder() {
        override val name: StringProperty = SimpleStringProperty("")
        override val currency: StringProperty =
            SimpleStringProperty(Currency.getInstance(Locale.getDefault()).currencyCode)

        override val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
                val name = name.value
                val unique = !store.containsClient(name)
                val validLength = name.length in 1..maxNameLength
                unique && validLength
            }, name)

        override fun toString(): String = name.value
    }

    inner class FilledClientBinder(internal val client: Client) :
        ClientBinder() {
        override val name: StringProperty = SimpleStringProperty(client.name)
        override val currency: StringProperty =
            SimpleStringProperty(client.currency.currencyCode)

        override val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
                val name = name.value
                val unchanged = client.isIdentifiedBy(name)
                val unique = !store.containsClient(name)
                val validLength = name.length in 1..maxNameLength
                (unchanged || unique) && validLength
            }, name)

        override fun toString(): String = client.name
    }
}
