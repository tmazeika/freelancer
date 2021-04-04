package me.mazeika.freelancer.binder.admin

import javafx.beans.binding.Bindings
import javafx.beans.property.*
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Node
import me.mazeika.freelancer.binder.services.DialogService
import me.mazeika.freelancer.model.Store
import me.mazeika.freelancer.model.Tag
import javax.inject.Inject

class TagsAdminBinder @Inject constructor(
    private val store: Store,
    private val dialogService: DialogService,
) {
    val tags: ObservableList<TagBinder> = FXCollections.observableArrayList()
    val selectedTag: ObjectProperty<TagBinder> = SimpleObjectProperty()
    val isEditDeleteVisible: BooleanProperty = SimpleBooleanProperty()

    init {
        isEditDeleteVisible.bind(selectedTag.isNotNull)
        updateTags()
    }

    fun onCreate(dialogViewFactory: (TagBinder) -> Node): Boolean {
        val binder = TagBinder()
        val ok = dialogService.prompt(
            title = "Create Tag",
            content = dialogViewFactory(binder),
            isValid = binder.isValid
        )
        if (ok) {
            store.addTag(Tag(binder.name.value))
            updateTags()
        }
        return ok
    }

    fun onEdit(dialogViewFactory: (TagBinder) -> Node): Boolean {
        val binder = selectedTag.value
        val ok = dialogService.prompt(
            title = "Edit Tag",
            content = dialogViewFactory(binder),
            isValid = binder.isValid
        )
        if (ok) {
            store.removeTag(binder.tag!!)
            store.addTag(Tag(binder.name.value))
            updateTags()
        }
        return ok
    }

    fun onDelete(): Boolean {
        val ok = dialogService.confirm(
            title = "Delete Tag",
            message = "Are you sure you want to delete this tag?"
        )
        if (ok) {
            store.removeTag(selectedTag.value.tag!!)
            updateTags()
        }
        return ok
    }

    private fun updateTags() {
        tags.setAll(store.getTags().map(::TagBinder))
    }

    inner class TagBinder(internal val tag: Tag? = null) {
        val name: StringProperty = SimpleStringProperty(tag?.name ?: "")

        internal val isValid = Bindings.createBooleanBinding({
            val unique = if (name.value != tag?.name) {
                !store.containsTag(name.value)
            } else true
            unique && name.value.length in 1..255
        }, name)

        override fun toString(): String = name.value
    }
}
