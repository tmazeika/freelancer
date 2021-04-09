package me.mazeika.freelancer.view.util

import javafx.scene.paint.Color

class ColorIndexBidiConverter(private val colors: List<Color>) :
    BidiBindings.Converter<Color, Number> {
    override fun to(a: Color): Int = colors.indexOf(a)

    override fun from(b: Number): Color = colors[b.toInt()]
}
