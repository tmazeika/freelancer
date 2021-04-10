package me.mazeika.freelancer.view.components

import javafx.scene.control.ContentDisplay
import javafx.scene.control.ListCell

class TextCellFactory<T>(private val getText: (T) -> String) : ListCell<T>() {
    init {
        contentDisplay = ContentDisplay.TEXT_ONLY
    }

    override fun updateItem(item: T?, empty: Boolean) {
        super.updateItem(item, empty)
        text = if (item != null && !empty) getText(item) else null
    }
}
