package me.mazeika.freelancer.view.components.forms

import javafx.application.Platform
import javafx.geometry.HPos
import javafx.scene.control.Label
import javafx.scene.layout.GridPane

class GridForm(vararg components: FormComponent) : GridPane() {
    init {
        hgap = 10.0
        vgap = 10.0
        components.forEachIndexed { i, component ->
            add(Label(component.label).apply {
                setHalignment(this, HPos.RIGHT)
            }, 0, i)
            add(component.node, 1, i)
            if (i == 0) {
                Platform.runLater { component.node.requestFocus() }
            }
        }
    }
}
