package me.mazeika.freelancer.binder.admin

import javafx.beans.property.*
import me.mazeika.freelancer.model.Project
import java.math.BigDecimal
import java.util.*

interface ProjectBinder {
    val client: ReadOnlyObjectProperty<SnapshotClientBinder>
    val name: ReadOnlyStringProperty
    val colorIndex: ReadOnlyObjectProperty<Int>
    val hourlyRate: ReadOnlyObjectProperty<BigDecimal>
    val currency: ReadOnlyObjectProperty<Currency>
}

data class SnapshotProjectBinder(internal val project: Project) :
    ProjectBinder {

    override val client: ReadOnlyObjectProperty<SnapshotClientBinder> =
        SimpleObjectProperty(SnapshotClientBinder(project.client))
    override val name: ReadOnlyStringProperty =
        SimpleStringProperty(project.name)
    override val colorIndex: ReadOnlyObjectProperty<Int> =
        SimpleObjectProperty(project.colorIndex)
    override val hourlyRate: ReadOnlyObjectProperty<BigDecimal> =
        SimpleObjectProperty(project.hourlyRate)
    override val currency: ReadOnlyObjectProperty<Currency> =
        SimpleObjectProperty(project.currency)
}

abstract class MutableProjectBinder(
    client: SnapshotClientBinder,
    name: String,
    colorIndex: Int,
    hourlyRate: BigDecimal,
    currency: Currency
) : ProjectBinder {

    override val client: ObjectProperty<SnapshotClientBinder> =
        SimpleObjectProperty(client)
    override val name: StringProperty = SimpleStringProperty(name)
    override val colorIndex: ObjectProperty<Int> =
        SimpleObjectProperty(colorIndex)
    override val hourlyRate: ObjectProperty<BigDecimal> =
        SimpleObjectProperty(hourlyRate)
    override val currency: ObjectProperty<Currency> =
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
