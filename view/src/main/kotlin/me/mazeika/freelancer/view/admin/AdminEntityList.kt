package me.mazeika.freelancer.view.admin

import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import me.mazeika.freelancer.binder.ListBinder
import me.mazeika.freelancer.binder.util.bindContent
import me.mazeika.freelancer.view.components.TextCellFactory

class AdminEntityList<T>(
    vm: ListBinder<T>,
    createCellFactory: (() -> ListCell<T>)? = null
) : ListView<T>() {
    init {
        if (createCellFactory != null) {
            setCellFactory { createCellFactory() }
        }
        items.bindContent(vm.items)
        vm.selected.bind(selectionModel.selectedItemProperty())
    }

    companion object {
        fun <T> useToString(vm: ListBinder<T>): AdminEntityList<T> =
            AdminEntityList(vm) { TextCellFactory.useToString() }
    }
}
