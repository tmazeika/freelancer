package me.mazeika.freelancer.view.util

import javafx.scene.paint.Color

class ColorIndexBidiConverter(private val colors: List<Color>) : BidiBindings.Converter<Color, Number> {
    override fun aToB(a: Color): Int = colors.indexOf(a)

    override fun bToA(b: Number): Color = colors[b.toInt()]
}
