package me.mazeika.freelancer.view.util

import javafx.scene.control.TextFormatter
import java.util.function.UnaryOperator

class MaxLengthFilter(private val maxLength: Int) :
    UnaryOperator<TextFormatter.Change?> {
    override fun apply(change: TextFormatter.Change?): TextFormatter.Change? {
        val text = change!!.controlNewText
        return if (text.length <= maxLength) change else null
    }
}
