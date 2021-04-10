package me.mazeika.freelancer.view.components

import com.google.inject.assistedinject.Assisted
import javafx.geometry.Pos
import javafx.scene.layout.HBox
import javafx.scene.shape.Circle
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import me.mazeika.freelancer.binder.admin.ProjectBinder
import me.mazeika.freelancer.binder.util.map
import me.mazeika.freelancer.view.services.ColorService
import javax.inject.Inject

class CompactProjectCell @Inject constructor(
    colorService: ColorService,
    @Assisted project: ProjectBinder
) : HBox() {
    init {
        alignment = Pos.CENTER_LEFT
        spacing = 10.0
        isFillHeight = false
        children.setAll(
            Circle(5.0).apply {
                fillProperty().bind(project.colorIndex.map { i ->
                    colorService.colors[i]
                })
            },
            TextFlow(
                Text().apply {
                    textProperty().bind(project.name)
                },
                Text("\n"),
                Text().apply {
                    styleClass += "muted-text"
                    textProperty().bind(project.client.map { it.name.value })
                }
            )
        )
    }
}
