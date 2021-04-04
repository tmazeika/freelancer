package me.mazeika.freelancer.view.components

import javafx.application.Platform
import javafx.beans.property.StringProperty
import javafx.scene.Node
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.scene.layout.GridPane
import me.mazeika.freelancer.view.util.MaxLengthFilter

class EntityForm(vararg components: Component) : GridPane() {
    init {
        hgap = 10.0
        vgap = 10.0
        components.forEachIndexed { i, component ->
            add(Label(component.name), 0, i)
            add(component.node, 1, i)
        }
    }

    interface Component {
        val name: String
        val node: Node
    }

    class Text(
        override val name: String,
        value: StringProperty,
        maxLength: Int = -1,
        initialFocus: Boolean = false
    ) : Component {
        override val node: Node = TextField().apply {
            if (initialFocus) {
                Platform.runLater { requestFocus() }
            }
            if (maxLength >= 0) {
                textFormatter = TextFormatter<TextFormatter.Change>(
                    MaxLengthFilter(maxLength)
                )
            }
            textProperty().bindBidirectional(value)
        }
    }

    class Combo(
        override val name: String,
        value: StringProperty,
        options: List<String>,
        initialFocus: Boolean = false
    ) : Component {
        override val node: Node = ComboBox<String>().apply {
            if (initialFocus) {
                Platform.runLater { requestFocus() }
            }
            items.setAll(options)
            valueProperty().bindBidirectional(value)
        }
    }
}
