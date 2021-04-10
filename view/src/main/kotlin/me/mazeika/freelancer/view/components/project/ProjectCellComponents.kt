package me.mazeika.freelancer.view.components.project

import javafx.scene.Node
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
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

class ProjectCellComponents @Inject constructor(
    private val i18nService: I18nService,
    private val colorService: ColorService
) {
    fun createIndicator(project: ProjectBinder): Node = Circle(5.0).apply {
        fillProperty().bind(project.colorIndex.map { i ->
            colorService.colors[i]
        })
    }

    fun createText(project: ProjectBinder): Node = TextFlow(
        Text().apply {
            textProperty().bind(project.name)
        },
        Text("\n"),
        Text().apply {
            fill = Color.GRAY
            textProperty().bind(project.client.map { it.name.value })
        }
    )

    fun createSpacer(): Node = Pane().apply {
        HBox.setHgrow(this, Priority.ALWAYS)
    }

    fun createRateText(project: ProjectBinder): Node = Text().apply {
        val rateText = bind({ hourlyRate, currency ->
            i18nService.formatMoney(hourlyRate, currency) + "/hr"
        }, project.hourlyRate, project.currency)
        visibleProperty().bind(project.hourlyRate.map {
            it != BigDecimal.ZERO
        })
        textProperty().bind(rateText)
    }
}
