package me.mazeika.freelancer.view.components

import javafx.scene.Node
import javafx.scene.control.ContentDisplay
import javafx.scene.control.ListCell

class TextThenGraphicCellFactory<T>(private val graphicFactory: (T) -> Node?) :
    ListCell<T>() {
    override fun updateItem(item: T?, empty: Boolean) {
        super.updateItem(item, empty)
        if (item == null || empty) {
            text = null
            graphic = null
        } else {
            val cell = graphicFactory(item)
            if (cell == null) {
                contentDisplay = null
                text = item.toString()
                graphic = null
            } else {
                contentDisplay = ContentDisplay.GRAPHIC_ONLY
                text = null
                graphic = cell
            }
        }
    }
}
