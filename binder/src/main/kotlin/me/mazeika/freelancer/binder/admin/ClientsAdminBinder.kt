package me.mazeika.freelancer.binder.admin

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableBooleanValue
import me.mazeika.freelancer.binder.services.DialogService
import me.mazeika.freelancer.model.Client
import me.mazeika.freelancer.model.Store
import javax.inject.Inject

class ClientsAdminBinder @Inject constructor(
    private val store: Store,
    dialogService: DialogService,
) : EntityAdminBinder<ClientsAdminBinder.ClientBinder, ClientsAdminBinder.FilledClientBinder>(
    entityName = "Client",
    dialogService
) {
    init {
        store.onClientsUpdated += ::updateClients
        updateClients()
    }

    override fun createEmptyBinder(): ClientBinder = EmptyClientBinder()

    override fun create(binder: ClientBinder) {
        store.addClient(Client(binder.name.value))
        updateClients()
    }

    override fun edit(binder: FilledClientBinder) {
        store.replaceClient(
            old = binder.client,
            new = Client(binder.name.value)
        )
        updateClients()
    }

    override fun delete(binder: FilledClientBinder) {
        store.removeClient(binder.client)
        updateClients()
    }

    private fun updateClients() {
        entities.setAll(store.getClients().map(::FilledClientBinder))
    }

    abstract class ClientBinder : EntityBinder {
        abstract val name: StringProperty
        val maxNameLength: Int = 128
    }

    private inner class EmptyClientBinder : ClientBinder() {
        override val name: StringProperty = SimpleStringProperty("")

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
