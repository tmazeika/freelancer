package me.mazeika.freelancer.view.components

import javafx.scene.Node
import javafx.scene.control.ContentDisplay
import javafx.scene.control.ListCell
import javafx.scene.text.Text

class TextCellFactory<T>(private val getText: (T) -> String) : ListCell<T>() {

    init {
        contentDisplay = ContentDisplay.TEXT_ONLY
    }

    override fun updateItem(item: T?, empty: Boolean) {
        super.updateItem(item, empty)
        text = if (item != null && !empty) getText(item) else null
    }

    companion object {
        fun <T> useToString(): TextCellFactory<T> =
            TextCellFactory { it.toString() }

        fun <T> useToString(obj: T): Node = Text(obj.toString())
    }
}
