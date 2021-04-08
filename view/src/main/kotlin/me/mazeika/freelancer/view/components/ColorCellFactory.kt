package me.mazeika.freelancer.view.components

import javafx.scene.control.ContentDisplay
import javafx.scene.control.ListCell
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class ColorCellFactory : ListCell<Color>() {
    init {
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
    }

    override fun updateItem(item: Color?, empty: Boolean) {
        super.updateItem(item, empty)
        graphic = if (item == null || empty) null else {
            Circle(5.0).apply {
                fill = item
            }
        }
    }
}
