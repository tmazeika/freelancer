package me.mazeika.freelancer.binder.admin

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import me.mazeika.freelancer.model.Tag

data class TagSnapshot(internal val tag: Tag) {
    val name: String = tag.name

    override fun toString(): String = name
}

abstract class TagBinder(name: String) {
    val name: StringProperty = SimpleStringProperty(name)

    val maxNameLength: Int = 32

    internal fun createTag(): Tag = Tag(name = name.value.trim())
}
