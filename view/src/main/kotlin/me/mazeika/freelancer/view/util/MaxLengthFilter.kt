package me.mazeika.freelancer.view.util

import javafx.scene.control.TextFormatter
import java.util.function.UnaryOperator

class MaxLengthFilter(private val maxLength: Int) : UnaryOperator<TextFormatter.Change?> {
    override fun apply(change: TextFormatter.Change?): TextFormatter.Change? {
        if (change!!.controlNewText.length > maxLength) {
            return null
        }
        return change
    }
}
