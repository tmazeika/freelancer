package me.mazeika.freelancer.view.admin

import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import me.mazeika.freelancer.binder.admin.EntityAdmin
import me.mazeika.freelancer.binder.util.bindContent

class EntityAdminList<T>(vm: EntityAdmin<T>, createCell: ListCell<T>? = null) :
    ListView<T>() {
    init {
        if (createCell != null) {
            setCellFactory { createCell }
        }
        items.bindContent(vm.entities)
        vm.selected.bind(selectionModel.selectedItemProperty())
    }
}
