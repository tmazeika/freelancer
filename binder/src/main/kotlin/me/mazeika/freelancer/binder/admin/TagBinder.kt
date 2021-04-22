package me.mazeika.freelancer.binder.admin

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import me.mazeika.freelancer.model.Tag
import java.util.*

data class TagSnapshot(internal val tag: Tag) {
    val id: UUID = tag.id
    val name: String = tag.name

    override fun toString(): String = name
}

abstract class TagBinder(val id: UUID, name: String) {
    val name: StringProperty = SimpleStringProperty(name)

    val maxNameLength: Int = 32

    internal fun createTag(): Tag = Tag(id = id, name = name.value.trim())
}
