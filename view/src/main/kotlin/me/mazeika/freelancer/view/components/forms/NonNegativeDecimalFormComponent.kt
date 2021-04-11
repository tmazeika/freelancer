package me.mazeika.freelancer.view.components.forms

import javafx.beans.property.Property
import javafx.scene.Node
import javafx.scene.control.TextField
import me.mazeika.freelancer.view.util.NonNegativeDecimalTextFormatter
import java.math.BigDecimal

class NonNegativeDecimalFormComponent(
    override val label: String,
    value: Property<BigDecimal>
) : TextField(), FormComponent {
    init {
        textFormatter = NonNegativeDecimalTextFormatter().apply {
            valueProperty().bindBidirectional(value)
        }
    }

    override val node: Node = this
}
