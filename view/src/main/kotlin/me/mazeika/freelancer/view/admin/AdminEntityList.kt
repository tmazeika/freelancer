package me.mazeika.freelancer.view.admin

import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import me.mazeika.freelancer.binder.admin.EntityListBinder
import me.mazeika.freelancer.binder.util.bindContent

class AdminEntityList<T>(
    vm: EntityListBinder<T>,
    createCellFactory: (() -> ListCell<T>)? = null
) : ListView<T>() {

    init {
        if (createCellFactory != null) {
            setCellFactory { createCellFactory() }
        }
        items.bindContent(vm.entities)
        vm.selected.bind(selectionModel.selectedItemProperty())
    }
}
