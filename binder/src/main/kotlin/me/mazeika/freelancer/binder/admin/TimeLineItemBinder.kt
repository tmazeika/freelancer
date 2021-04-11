package me.mazeika.freelancer.binder.admin

import com.google.common.collect.ImmutableSet
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import me.mazeika.freelancer.model.TimeLineItem
import java.time.Instant
import java.util.*

data class TimeLineItemSnapshot(internal val timeLineItem: TimeLineItem) {
    val id: UUID = timeLineItem.id
    val project: ProjectSnapshot = ProjectSnapshot(timeLineItem.project)
    val name: String = timeLineItem.name
    val tags: ImmutableSet<TagSnapshot> =
        ImmutableSet.copyOf(timeLineItem.tags.map(::TagSnapshot))
    val start: Instant = timeLineItem.start
    val end: Instant? = timeLineItem.end
}

abstract class TimeLineItemBinder(
    val id: UUID,
    project: ProjectSnapshot,
    name: String,
    tags: ImmutableSet<TagSnapshot>,
    start: Instant,
    end: Instant?
) {
    val project: ObjectProperty<ProjectSnapshot> = SimpleObjectProperty(project)
    val name: StringProperty = SimpleStringProperty(name)
    val tags: ObservableList<TagSnapshot> =
        FXCollections.observableList(tags.toMutableList())
    val start: ObjectProperty<Instant> = SimpleObjectProperty(start)
    val end: ObjectProperty<Instant> = SimpleObjectProperty(end)

    val maxNameLength: Int = 128

    internal fun createTimeLineItem(): TimeLineItem = TimeLineItem(
        id = id,
        project = project.value.project,
        name = name.value.trim(),
        tags = ImmutableSet.copyOf(tags.map { it.tag }),
        start = start.value,
        end = end.value
    )
}
