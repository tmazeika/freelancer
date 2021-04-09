package me.mazeika.freelancer.view.components.forms

import javafx.beans.property.Property
import javafx.scene.Node
import javafx.scene.control.ComboBox

class OptionsFormComponent<T>(
    override val label: String,
    value: Property<T>,
    options: Collection<T>
) : ComboBox<T>(), FormComponent {

    init {
        items.setAll(options)
        valueProperty().bindBidirectional(value)
    }

    override val node: Node = this
}
