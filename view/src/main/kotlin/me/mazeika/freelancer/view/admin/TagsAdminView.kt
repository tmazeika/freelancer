package me.mazeika.freelancer.view.admin

import javafx.application.Platform
import javafx.beans.binding.Bindings
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.GridPane
import me.mazeika.freelancer.binder.admin.TagsAdminBinder
import javax.inject.Inject

class TagsAdminView @Inject constructor(vm: TagsAdminBinder) : BorderPane() {
    init {
        val tagList = ListView<TagsAdminBinder.TagBinder>().apply {
            Bindings.bindContent(items, vm.tags)
            vm.selectedTag.bind(selectionModel.selectedItemProperty())
        }
        val actionBar = FlowPane().apply {
            val createBtn = Button("Create").apply {
                setOnAction { vm.onCreate(::TagView) }
            }
            val editBtn = Button("Edit").apply {
                setOnAction { vm.onEdit(::TagView) }
                visibleProperty().bind(vm.isEditDeleteVisible)
            }
            val deleteBtn = Button("Delete").apply {
                setOnAction { vm.onDelete() }
                visibleProperty().bind(vm.isEditDeleteVisible)
            }
            children.addAll(createBtn, editBtn, deleteBtn)
        }
        top = actionBar
        center = tagList
    }

    class TagView(vm: TagsAdminBinder.TagBinder) : GridPane() {
        init {
            hgap = 10.0
            vgap = 10.0
            val nameInput = TextField().apply {
                Platform.runLater { requestFocus() }
                textProperty().bindBidirectional(vm.name)
            }
            add(Label("Name"), 0, 0)
            add(nameInput, 1, 0)
        }
    }
}
