package me.mazeika.freelancer.view.util

import com.google.common.base.Converter
import javafx.scene.paint.Color

class ColorIndexConverter(private val colors: List<Color>) :
    Converter<Int, Color>() {

    override fun doForward(b: Int): Color = colors[b]

    override fun doBackward(a: Color): Int = colors.indexOf(a)
}
