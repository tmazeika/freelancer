package me.mazeika.freelancer.binder.admin

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import me.mazeika.freelancer.model.Project
import java.math.BigDecimal
import java.util.*

data class ProjectSnapshot(internal val project: Project) {
    val client: ClientSnapshot = ClientSnapshot(project.client)
    val name: String = project.name
    val colorIndex: Int = project.colorIndex
    val hourlyRate: BigDecimal = project.hourlyRate
    val currency: Currency = project.currency

    override fun toString(): String = name
}

abstract class ProjectBinder(
    client: ClientSnapshot,
    name: String,
    colorIndex: Int,
    hourlyRate: BigDecimal,
    currency: Currency
) {
    val client: ObjectProperty<ClientSnapshot> =
        SimpleObjectProperty(client)
    val name: StringProperty = SimpleStringProperty(name)
    val colorIndex: ObjectProperty<Int> =
        SimpleObjectProperty(colorIndex)
    val hourlyRate: ObjectProperty<BigDecimal> =
        SimpleObjectProperty(hourlyRate)
    val currency: ObjectProperty<Currency> =
        SimpleObjectProperty(currency)

    val maxNameLength: Int = 128

    internal fun createProject(): Project = Project(
        client = client.value.client,
        name = name.value.trim(),
        colorIndex = colorIndex.value,
        hourlyRate = hourlyRate.value,
        currency = currency.value
    )
}
