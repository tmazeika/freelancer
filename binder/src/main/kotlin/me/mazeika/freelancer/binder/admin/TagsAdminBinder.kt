package me.mazeika.freelancer.binder.admin

import javafx.beans.binding.Bindings
import javafx.beans.value.ObservableBooleanValue
import javafx.scene.Node
import me.mazeika.freelancer.binder.services.DialogService
import me.mazeika.freelancer.binder.util.bindContent
import me.mazeika.freelancer.model.Store
import me.mazeika.freelancer.model.Tag
import javax.inject.Inject

class TagsAdminBinder @Inject constructor(
    private val store: Store,
    private val dialogService: DialogService
) : AdminBinder<TagBinder, TagSnapshot>() {

    init {
        items.bindContent(store.tags, ::TagSnapshot)
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
            message = "Are you sure you want to delete \"${binder.name.value}\"?"
        )
        if (ok) {
            store.removeTag(binder.tag)
        }
        return ok
    }

    private inner class EmptyTagBinder : TagBinder(name = "") {
        val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
                val name = name.value.trim()
                val isUnique = !store.containsTag(name)
                val isNameValid = name.isNotEmpty()
                isUnique && isNameValid
            }, name)
    }

    private inner class FilledTagBinder(val tag: Tag) :
        TagBinder(name = tag.name) {

        val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
                val name = name.value.trim()
                val isUnchanged = tag.isIdentifiedBy(name)
                val isUnique = !store.containsTag(name)
                val isNameValid = name.isNotEmpty()
                (isUnchanged || isUnique) && isNameValid
            }, name)
    }
}
