package me.mazeika.freelancer.binder.admin

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableBooleanValue
import me.mazeika.freelancer.binder.services.DialogService
import me.mazeika.freelancer.model.Store
import me.mazeika.freelancer.model.Tag
import javax.inject.Inject

class TagsAdminBinder @Inject constructor(
    private val store: Store,
    dialogService: DialogService,
) : EntityAdminBinder<TagsAdminBinder.TagBinder, TagsAdminBinder.FilledTagBinder>(
    entityName = "Tag",
    dialogService
) {
    init {
        store.onTagsUpdated += {
            entities.setAll(store.getTags().map(::FilledTagBinder))
        }
    }

    override fun createEmptyBinder(): TagBinder = EmptyTagBinder()

    override fun copyFilledBinder(binder: FilledTagBinder): FilledTagBinder =
        FilledTagBinder(binder.tag)

    override fun create(binder: TagBinder) {
        store.addTag(binder.createTag())
    }

    override fun edit(binder: FilledTagBinder) {
        store.replaceTag(old = binder.tag, new = binder.createTag())
    }

    override fun delete(binder: FilledTagBinder) {
        store.removeTag(binder.tag)
    }

    abstract class TagBinder : EntityBinder {
        abstract val name: StringProperty
        val maxNameLength: Int = 32

        internal fun createTag(): Tag = Tag(name.value)
    }

    private inner class EmptyTagBinder : TagBinder() {
        override val name: StringProperty = SimpleStringProperty("")

        override val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
                val name = name.value
                val unique = !store.containsTag(name)
                val validLength = name.length in 1..maxNameLength
                unique && validLength
            }, name)

        override fun toString(): String = name.value
    }

    inner class FilledTagBinder(internal val tag: Tag) : TagBinder() {
        override val name: StringProperty = SimpleStringProperty(tag.name)

        override val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
                val name = name.value
                val unchanged = tag.isIdentifiedBy(name)
                val unique = !store.containsTag(name)
                val validLength = name.length in 1..maxNameLength
                (unchanged || unique) && validLength
            }, name)

        override fun toString(): String = tag.name
    }
}
