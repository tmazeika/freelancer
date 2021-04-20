package me.mazeika.freelancer.view.util

import javafx.scene.control.TextFormatter
import javafx.util.StringConverter
import java.math.BigDecimal
import java.util.function.UnaryOperator
import java.util.regex.Pattern

class NonNegativeDecimalTextFormatter :
    TextFormatter<BigDecimal>(Converter(), BigDecimal.ZERO, Filter()) {

    class Converter : StringConverter<BigDecimal>() {

        override fun toString(v: BigDecimal): String = v.toPlainString()

        override fun fromString(v: String): BigDecimal = BigDecimal(v)
    }

    class Filter : UnaryOperator<Change?> {

        override fun apply(change: Change?): Change? {
            val text = change!!.controlNewText
            val isInvalid = !filterPattern.matcher(text).matches()
                    || text.length > 128
            return if (isInvalid) null else change
        }
    }

    companion object {
        private val filterPattern = Pattern.compile("[0-9]*\\.?[0-9]{0,32}")
    }
}
