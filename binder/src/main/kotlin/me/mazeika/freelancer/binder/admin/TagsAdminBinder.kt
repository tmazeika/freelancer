package me.mazeika.freelancer.binder.admin

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableBooleanValue
import javafx.scene.Node
import me.mazeika.freelancer.binder.services.DialogService
import me.mazeika.freelancer.model.Store
import me.mazeika.freelancer.model.Tag
import javax.inject.Inject

class TagsAdminBinder @Inject constructor(
    private val store: Store,
    private val dialogService: DialogService
) : AdminBinder<TagsAdminBinder.TagBinder, TagsAdminBinder.FilledTagBinder>() {

    init {
        store.onTagsUpdated += {
            entities.setAll(store.getTags().map(::FilledTagBinder))
        }
    }

    override fun onCreate(dialogViewFactory: (TagBinder) -> Node): Boolean {
        val binder = EmptyTagBinder()
        val ok = dialogService.prompt(
            title = "Create Tag",
            content = dialogViewFactory(binder),
            isValid = binder.isValid
        )
        if (ok) {
            store.addTag(binder.createTag())
        }
        return ok
    }

    override fun onEdit(dialogViewFactory: (TagBinder) -> Node): Boolean {
        val binder = FilledTagBinder(selected.value.tag)
        val ok = dialogService.prompt(
            title = "Edit Tag",
            content = dialogViewFactory(binder),
            isValid = binder.isValid
        )
        if (ok) {
            store.replaceTag(old = binder.tag, new = binder.createTag())
        }
        return ok
    }

    override fun onDelete(): Boolean {
        val binder = FilledTagBinder(selected.value.tag)
        val ok = dialogService.confirm(
            title = "Delete Tag",
            message = """Are you sure you want to delete "$binder"?""".trimEnd()
        )
        if (ok) {
            store.removeTag(binder.tag)
        }
        return ok
    }

    abstract class TagBinder(name: String) : EntityBinder {

        val name: StringProperty = SimpleStringProperty(name)
        val maxNameLength: Int = 32

        internal fun createTag(): Tag = Tag(name = name.value.trim())
    }

    private inner class EmptyTagBinder : TagBinder(name = "") {

        override val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
                val name = name.value.trim()
                val isUnique = !store.containsTag(name)
                val isNameValid = name.isNotEmpty()
                isUnique && isNameValid
            }, name)

        override fun toString(): String = name.value
    }

    inner class FilledTagBinder(internal val tag: Tag) :
        TagBinder(name = tag.name) {

        override val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
                val name = name.value.trim()
                val isUnchanged = tag.isIdentifiedBy(name)
                val isUnique = !store.containsTag(name)
                val isNameValid = name.isNotEmpty()
                (isUnchanged || isUnique) && isNameValid
            }, name)

        override fun toString(): String = tag.name
    }
}
