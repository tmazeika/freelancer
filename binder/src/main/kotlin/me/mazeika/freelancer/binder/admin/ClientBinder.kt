package me.mazeika.freelancer.binder.admin

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import me.mazeika.freelancer.model.Client
import java.util.*

data class ClientSnapshot(internal val client: Client) {
    val id: UUID = client.id
    val name: String = client.name
    val currency: Currency = client.currency

    override fun toString(): String = name
}

abstract class ClientBinder(val id: UUID, name: String, currency: Currency) {
    val name: StringProperty = SimpleStringProperty(name)
    val currency: ObjectProperty<Currency> = SimpleObjectProperty(currency)

    val maxNameLength: Int = 128

    internal fun createClient(): Client = Client(
        id = id,
        name = name.value.trim(),
        currency = currency.value
    )
}
