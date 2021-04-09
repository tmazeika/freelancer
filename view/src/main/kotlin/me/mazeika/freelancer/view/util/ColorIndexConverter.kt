package me.mazeika.freelancer.view.util

import javafx.scene.paint.Color

class ColorIndexConverter(private val colors: List<Color>) :
    BidiBindings.Converter<Color, Int> {

    override fun to(a: Color): Int = colors.indexOf(a)

    override fun from(b: Int): Color = colors[b]
}
