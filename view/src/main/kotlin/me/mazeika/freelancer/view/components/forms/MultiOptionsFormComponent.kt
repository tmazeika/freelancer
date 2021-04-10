package me.mazeika.freelancer.view.components.forms

import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import me.mazeika.freelancer.binder.util.bindContent

class MultiOptionsFormComponent<T>(
    override val label: String,
    values: ObservableList<T>,
    options: Collection<T>,
    createCell: (() -> ListCell<T>)? = null
) : ListView<T>(), FormComponent {

    init {
        if (createCell != null) {
            setCellFactory { createCell() }
        }
        selectionModel.selectionMode = SelectionMode.MULTIPLE
        items.setAll(options)
        values.bindContent(selectionModel.selectedItems)
    }

    override val node: Node = this
}
