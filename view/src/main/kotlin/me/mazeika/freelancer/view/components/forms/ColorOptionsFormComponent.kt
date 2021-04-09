package me.mazeika.freelancer.view.components.forms

import javafx.beans.property.Property
import javafx.scene.Node
import javafx.scene.control.ComboBox
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import me.mazeika.freelancer.view.components.GraphicCellFactory

class ColorOptionsFormComponent(
    override val label: String,
    value: Property<Color>,
    options: Collection<Color>
) : ComboBox<Color>(), FormComponent {

    init {
        val createColorCellFactory = {
            GraphicCellFactory<Color> { color ->
                Circle(5.0).apply {
                    fill = color
                }
            }
        }
        buttonCell = createColorCellFactory()
        setCellFactory { createColorCellFactory() }
        items.setAll(options)
        valueProperty().bindBidirectional(value)
    }

    override val node: Node = this
}
