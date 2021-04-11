package me.mazeika.freelancer.view.components.forms

import javafx.beans.property.Property
import javafx.scene.Node
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import me.mazeika.freelancer.view.util.MaxLengthFilter

class TextFormComponent(
    override val label: String,
    value: Property<String>,
    maxLength: Int? = null
) : TextField(), FormComponent {
    init {
        if (maxLength != null) {
            textFormatter =
                TextFormatter<TextFormatter.Change>(MaxLengthFilter(maxLength))
        }
        textProperty().bindBidirectional(value)
    }

    override val node: Node = this
}
