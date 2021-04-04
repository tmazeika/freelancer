package me.mazeika.freelancer.binder.admin

import javafx.beans.property.BooleanProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableBooleanValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Node
import me.mazeika.freelancer.binder.services.DialogService

abstract class EntityAdminBinder<EB, FilledEB>(
    private val entityName: String,
    private val dialogService: DialogService
) where EB : EntityAdminBinder.EntityBinder, FilledEB : EB {
    val entities: ObservableList<FilledEB> = FXCollections.observableArrayList()
    val selected: ObjectProperty<FilledEB> = SimpleObjectProperty()
    val isEditDeleteVisible: BooleanProperty = SimpleBooleanProperty()

    init {
        isEditDeleteVisible.bind(selected.isNotNull)
    }

    fun onCreate(dialogViewFactory: (EB) -> Node): Boolean {
        val binder = createEmptyBinder()
        val ok = dialogService.prompt(
            title = "Create $entityName",
            content = dialogViewFactory(binder),
            isValid = binder.isValid
        )
        if (ok) create(binder)
        return ok
    }

    protected abstract fun createEmptyBinder(): EB

    protected abstract fun create(binder: EB)

    fun onEdit(dialogViewFactory: (EB) -> Node): Boolean {
        val binder = selected.value!!
        val ok = dialogService.prompt(
            title = "Edit $entityName",
            content = dialogViewFactory(binder),
            isValid = binder.isValid
        )
        if (ok) edit(binder)
        return ok
    }

    protected abstract fun edit(binder: FilledEB)

    fun onDelete(): Boolean {
        val binder = selected.value!!
        val ok = dialogService.confirm(
            title = "Delete $entityName",
            message = """Are you sure you want to delete "$binder"?"""
        )
        if (ok) delete(binder)
        return ok
    }

    protected abstract fun delete(binder: FilledEB)

    interface EntityBinder {
        val isValid: ObservableBooleanValue

        override fun toString(): String
    }
}
