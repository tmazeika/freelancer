package me.mazeika.freelancer.view.admin

import javafx.beans.binding.Bindings
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import me.mazeika.freelancer.binder.admin.EntityAdminBinder
import me.mazeika.freelancer.view.components.TextThenGraphicCellFactory

abstract class EntityAdminView<EB, FilledEB>(vm: EntityAdminBinder<EB, FilledEB>) :
    BorderPane() where EB : EntityAdminBinder.EntityBinder, FilledEB : EB {
    init {
        val list = ListView<FilledEB>().apply {
            setCellFactory { TextThenGraphicCellFactory { createListCell(it) } }
            Bindings.bindContent(items, vm.entities)
            vm.selected.bind(selectionModel.selectedItemProperty())
        }
        val actionBar = FlowPane().apply {
            hgap = 10.0
            vgap = 10.0
            padding = Insets(10.0)
            val createBtn = Button("Create").apply {
                setOnAction { vm.onCreate(::createEntityView) }
            }
            val editBtn = Button("Edit").apply {
                setOnAction { vm.onEdit(::createEntityView) }
                visibleProperty().bind(vm.isEditDeleteVisible)
            }
            val deleteBtn = Button("Delete").apply {
                setOnAction { vm.onDelete() }
                visibleProperty().bind(vm.isEditDeleteVisible)
            }
            children.addAll(createBtn, editBtn, deleteBtn)
        }
        top = actionBar
        center = list
    }

    open fun createListCell(item: EB): Node? = null

    abstract fun createEntityView(vm: EB): Node
}
