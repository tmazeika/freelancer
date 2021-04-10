package me.mazeika.freelancer.binder.admin

import javafx.beans.property.*
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import me.mazeika.freelancer.model.LineItem
import me.mazeika.freelancer.model.TimeLineItem
import java.time.Instant
import java.util.*

interface LineItemBinder {
    val name: ReadOnlyStringProperty
    val project: ReadOnlyObjectProperty<SnapshotProjectBinder>
    val tags: ObservableList<SnapshotTagBinder>
    val time: ReadOnlyObjectProperty<Instant>
}

data class SnapshotLineItemBinder(internal val lineItem: LineItem) :
    LineItemBinder {
    override val name: ReadOnlyStringProperty =
        SimpleStringProperty(lineItem.name)
    override val project: ReadOnlyObjectProperty<SnapshotProjectBinder> =
        SimpleObjectProperty(SnapshotProjectBinder(lineItem.project))
    override val tags: ObservableList<SnapshotTagBinder> =
        FXCollections.unmodifiableObservableList(
            FXCollections.observableArrayList(lineItem.tags.map(::SnapshotTagBinder))
        )
    override val time: ReadOnlyObjectProperty<Instant> =
        SimpleObjectProperty(lineItem.time)
}

abstract class MutableLineItemBinder(
    private val id: UUID,
    name: String,
    project: SnapshotProjectBinder,
    tags: List<SnapshotTagBinder>,
    time: Instant
) : LineItemBinder {

    override val name: StringProperty = SimpleStringProperty(name)
    override val project: ObjectProperty<SnapshotProjectBinder> =
        SimpleObjectProperty(project)
    override val tags: ObservableList<SnapshotTagBinder> =
        FXCollections.observableArrayList(tags)
    override val time: ObjectProperty<Instant> = SimpleObjectProperty(time)
    val maxNameLength: Int = 128

    internal fun createLineItem(): LineItem = TimeLineItem(
        id = id,
        name = name.value.trim(),
        project = project.value.project,
        tags = tags.map(SnapshotTagBinder::tag),
        start = time.value,
        end = null
    )
}
