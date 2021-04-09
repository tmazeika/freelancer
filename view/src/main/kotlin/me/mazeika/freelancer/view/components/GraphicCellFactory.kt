package me.mazeika.freelancer.view.components

import javafx.scene.Node
import javafx.scene.control.ContentDisplay
import javafx.scene.control.ListCell

class GraphicCellFactory<T>(private val createGraphic: (T) -> Node) :
    ListCell<T>() {

    init {
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
    }

    override fun updateItem(item: T?, empty: Boolean) {
        super.updateItem(item, empty)
        graphic = if (item != null && !empty) createGraphic(item) else null
    }
}
