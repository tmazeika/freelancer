package me.mazeika.freelancer.view.components

import javafx.geometry.Insets
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import me.mazeika.freelancer.binder.admin.TagSnapshot

class TagDisplay(tag: TagSnapshot) :
    TextFlow(Text(tag.name).apply { fill = Color.DARKBLUE }) {
    init {
        padding = Insets(2.0, 8.0, 2.0, 8.0)
        border = Border(
            BorderStroke(
                Color.CORNFLOWERBLUE,
                BorderStrokeStyle.SOLID,
                CornerRadii(15.0),
                BorderWidths.DEFAULT
            )
        )
        background = Background(
            BackgroundFill(
                Color.AZURE,
                CornerRadii(15.0),
                Insets.EMPTY
            )
        )
    }
}
