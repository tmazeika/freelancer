package me.mazeika.freelancer.view.components.forms

import javafx.beans.property.Property
import javafx.scene.Node
import javafx.scene.control.ComboBox
import javafx.scene.control.ListCell

class OptionsFormComponent<T>(
    override val label: String,
    value: Property<T>,
    options: Collection<T>,
    createCell: (() -> ListCell<T>)? = null,
    createButtonCell: (() -> ListCell<T>)? = null
) : ComboBox<T>(), FormComponent {
    init {
        if (createButtonCell != null || createCell != null) {
            buttonCell = (createButtonCell ?: createCell)!!()
        }
        if (createCell != null) {
            setCellFactory { createCell() }
        }
        items.setAll(options)
        valueProperty().bindBidirectional(value)
    }

    override val node: Node = this
}
