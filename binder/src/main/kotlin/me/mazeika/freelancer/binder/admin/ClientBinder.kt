package me.mazeika.freelancer.binder.admin

import javafx.beans.property.*
import me.mazeika.freelancer.model.Client
import java.util.*

interface ClientBinder {
    val name: ReadOnlyStringProperty
    val currency: ReadOnlyObjectProperty<Currency>
}

data class SnapshotClientBinder(internal val client: Client) : ClientBinder {
    override val name: ReadOnlyStringProperty =
        SimpleStringProperty(client.name)
    override val currency: ReadOnlyObjectProperty<Currency> =
        SimpleObjectProperty(client.currency)
}

abstract class MutableClientBinder(name: String, currency: Currency) :
    ClientBinder {

    override val name: StringProperty = SimpleStringProperty(name)
    override val currency: ObjectProperty<Currency> =
        SimpleObjectProperty(currency)
    val maxNameLength: Int = 128

    internal fun createClient(): Client =
        Client(name = name.value.trim(), currency = currency.value)
}
