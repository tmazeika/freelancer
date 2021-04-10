package me.mazeika.freelancer.binder.admin

import javafx.beans.property.ReadOnlyStringProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import me.mazeika.freelancer.model.Tag

interface TagBinder {
    val name: ReadOnlyStringProperty
}

data class SnapshotTagBinder(internal val tag: Tag) : TagBinder {
    override val name: ReadOnlyStringProperty = SimpleStringProperty(tag.name)
}

abstract class MutableTagBinder(name: String) : TagBinder {
    override val name: StringProperty = SimpleStringProperty(name)
    val maxNameLength: Int = 32

    internal fun createTag(): Tag = Tag(name = name.value.trim())
}
