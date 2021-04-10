package me.mazeika.freelancer.view.components

import com.google.inject.assistedinject.Assisted
import javafx.geometry.Pos
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.shape.Circle
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import me.mazeika.freelancer.binder.admin.ProjectBinder
import me.mazeika.freelancer.binder.i18n.I18nService
import me.mazeika.freelancer.binder.util.bind
import me.mazeika.freelancer.binder.util.map
import me.mazeika.freelancer.view.services.ColorService
import java.math.BigDecimal
import javax.inject.Inject

class ProjectCell @Inject constructor(
    colorService: ColorService,
    i18nService: I18nService,
    @Assisted project: ProjectBinder
) : HBox() {
    init {
        alignment = Pos.CENTER_LEFT
        spacing = 10.0
        val rateText = bind({ hourlyRate, currency ->
            i18nService.formatMoney(hourlyRate, currency) + "/hr"
        }, project.hourlyRate, project.currency)
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
            ),
            Pane().apply {
                setHgrow(this, Priority.ALWAYS)
            },
            Text().apply {
                visibleProperty().bind(project.hourlyRate.map {
                    it != BigDecimal.ZERO
                })
                textProperty().bind(rateText)
            }
        )
    }
}
