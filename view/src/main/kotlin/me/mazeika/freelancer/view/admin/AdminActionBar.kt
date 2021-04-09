package me.mazeika.freelancer.view.admin

import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.layout.FlowPane
import me.mazeika.freelancer.binder.admin.EntityActionBinder

class AdminActionBar<T>(
    vm: EntityActionBinder<T>,
    createDialogView: (T) -> Node
) : FlowPane() {

    init {
        hgap = 10.0
        vgap = 10.0
        padding = Insets(10.0)
        children.setAll(
            Button("Create").apply {
                setOnAction { vm.onCreate(createDialogView) }
            },
            Button("Edit").apply {
                setOnAction { vm.onEdit(createDialogView) }
                visibleProperty().bind(vm.isEditDeleteVisible)
            },
            Button("Delete").apply {
                setOnAction { vm.onDelete() }
                visibleProperty().bind(vm.isEditDeleteVisible)
            }
        )
    }
}
