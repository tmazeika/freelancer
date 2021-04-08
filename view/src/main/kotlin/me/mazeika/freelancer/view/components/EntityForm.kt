package me.mazeika.freelancer.view.components

import javafx.application.Platform
import javafx.beans.property.Property
import javafx.beans.property.StringProperty
import javafx.scene.Node
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import me.mazeika.freelancer.view.util.MaxLengthFilter
import me.mazeika.freelancer.view.util.NonNegativeDecimalTextFormatter
import java.math.BigDecimal

class EntityForm(vararg components: Component) : GridPane() {
    init {
        hgap = 10.0
        vgap = 10.0
        components.forEachIndexed { i, component ->
            add(Label(component.name), 0, i)
            add(component.node, 1, i)
            if (i == 0) {
                Platform.runLater { component.node.requestFocus() }
            }
        }
    }

    interface Component {
        val name: String
        val node: Node
    }

    class TextInput(
        override val name: String,
        value: StringProperty,
        maxLength: Int? = null
    ) : Component {
        override val node: Node = TextField().apply {
            if (maxLength != null) {
                textFormatter = TextFormatter<TextFormatter.Change>(
                    MaxLengthFilter(maxLength)
                )
            }
            textProperty().bindBidirectional(value)
        }
    }

    class NonNegativeDecimalInput(
        override val name: String,
        value: Property<BigDecimal>
    ) : Component {
        override val node: Node = TextField().apply {
            textFormatter = NonNegativeDecimalTextFormatter().apply {
                valueProperty().bindBidirectional(value)
            }
        }
    }

    class ComboInput<T> (
        override val name: String,
        value: Property<T>,
        options: List<T>
    ) : Component {
        override val node: Node = ComboBox<T>().apply {
            items.setAll(options)
            valueProperty().bindBidirectional(value)
        }
    }

    class ColorComboInput(
        override val name: String,
        value: Property<Color>,
        options: List<Color>
    ) : Component {
        override val node: Node = ComboBox<Color>().apply {
            setCellFactory { ColorCellFactory() }
            buttonCell = ColorCellFactory()
            items.setAll(options)
            valueProperty().bindBidirectional(value)
        }
    }
}
