package me.mazeika.freelancer.binder.admin

import javafx.beans.binding.Bindings
import javafx.beans.property.*
import javafx.beans.value.ObservableBooleanValue
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
) : EntityActionHandler<TagsAdminBinder.TagBinder>,
    EntityAdmin<TagsAdminBinder.FilledTagBinder> {

    override val entities: ObservableList<FilledTagBinder> =
        FXCollections.observableArrayList()
    override val selected: ObjectProperty<FilledTagBinder> =
        SimpleObjectProperty()
    override val isEditDeleteVisible: BooleanProperty = SimpleBooleanProperty()

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

        internal fun createTag(): Tag = Tag(name.value)
    }

    private inner class EmptyTagBinder : TagBinder(name = "") {
        override val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
                val name = name.value
                val unique = !store.containsTag(name)
                val validLength = name.length in 1..maxNameLength
                unique && validLength
            }, name)

        override fun toString(): String = name.value
    }

    inner class FilledTagBinder(internal val tag: Tag) :
        TagBinder(name = tag.name) {
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
