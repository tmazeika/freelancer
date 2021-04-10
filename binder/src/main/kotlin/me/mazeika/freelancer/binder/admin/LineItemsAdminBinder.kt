package me.mazeika.freelancer.binder.admin

import javafx.beans.binding.Bindings
import javafx.beans.value.ObservableBooleanValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Node
import me.mazeika.freelancer.binder.services.DialogService
import me.mazeika.freelancer.model.LineItem
import me.mazeika.freelancer.model.Store
import java.time.Instant
import java.util.*
import javax.inject.Inject

class LineItemsAdminBinder @Inject constructor(
    private val store: Store,
    private val dialogService: DialogService
) : AdminBinder<MutableLineItemBinder, LineItemsAdminBinder.FilledLineItemBinder>() {

    val allProjects: ObservableList<SnapshotProjectBinder> =
        FXCollections.observableArrayList()
    val allTags: ObservableList<SnapshotTagBinder> =
        FXCollections.observableArrayList()

    init {
        store.onProjectsUpdated += {
            allProjects.setAll(store.getProjects().map(::SnapshotProjectBinder))
            isCreateVisible.value = store.getProjects().isNotEmpty()
        }
        store.onTagsUpdated += {
            allTags.setAll((store.getTags().map(::SnapshotTagBinder)))
        }
        store.onLineItemsUpdated += {
            items.setAll(store.getLineItems().map(::FilledLineItemBinder))
        }
    }

    override fun onCreate(dialogViewFactory: (MutableLineItemBinder) -> Node): Boolean {
        val binder = EmptyLineItemBinder()
        val ok = dialogService.prompt(
            title = "Create Line Item",
            content = dialogViewFactory(binder),
            isValid = binder.isValid
        )
        if (ok) {
            store.addLineItem(binder.createLineItem())
        }
        return ok
    }

    override fun onEdit(dialogViewFactory: (MutableLineItemBinder) -> Node): Boolean {
        val binder = FilledLineItemBinder(selected.value.lineItem)
        val ok = dialogService.prompt(
            title = "Edit Line Item",
            content = dialogViewFactory(binder),
            isValid = binder.isValid
        )
        if (ok) {
            store.replaceLineItem(
                old = binder.lineItem,
                new = binder.createLineItem()
            )
        }
        return ok
    }

    override fun onDelete(): Boolean {
        val binder = FilledLineItemBinder(selected.value.lineItem)
        val ok = dialogService.confirm(
            title = "Delete Line Item",
            message = "Are you sure you want to delete \"${binder.name}\"?"
        )
        if (ok) {
            store.removeLineItem(binder.lineItem)
        }
        return ok
    }

    private inner class EmptyLineItemBinder : MutableLineItemBinder(
        id = UUID.randomUUID(),
        name = "",
        project = SnapshotProjectBinder(store.getProjects()[0]),
        tags = emptyList(),
        time = Instant.now()
    ) {
        val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
//                val name = name.value.trim()
//                val isUnique = !store.containsClient(name)
//                val isNameValid = name.isNotEmpty()
//                isUnique && isNameValid
                true
            }, name)
    }

    inner class FilledLineItemBinder(internal val lineItem: LineItem) :
        MutableLineItemBinder(
            id = lineItem.id,
            name = lineItem.name,
            project = SnapshotProjectBinder(lineItem.project),
            tags = lineItem.tags.map(::SnapshotTagBinder),
            time = lineItem.time
        ) {

        val isValid: ObservableBooleanValue =
            Bindings.createBooleanBinding({
//                val name = name.value.trim()
//                val isUnchanged = client.isIdentifiedBy(name)
//                val isUnique = !store.containsClient(name)
//                val isNameValid = name.isNotEmpty()
//                (isUnchanged || isUnique) && isNameValid
                true
            }, name)
    }
}
