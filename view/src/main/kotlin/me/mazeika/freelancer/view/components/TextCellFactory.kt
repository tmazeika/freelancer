package me.mazeika.freelancer.view.components

import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.ContentDisplay
import javafx.scene.control.ListCell
import javafx.scene.text.Text

class TextCellFactory<T>(private val getText: (T) -> String) : ListCell<T>() {

    init {
        contentDisplay = ContentDisplay.TEXT_ONLY
        padding = Insets(10.0)
    }

    override fun updateItem(item: T?, empty: Boolean) {
        super.updateItem(item, empty)
        text = if (item != null && !empty) getText(item) else null
    }

    companion object {
        fun <T> useToString(): TextCellFactory<T> =
            TextCellFactory { it.toString() }

        fun <T> useToString(obj: T): Node = Text(obj.toString()).apply {
            lineSpacing = 5.0
        }
    }
}
