package me.mazeika.freelancer.view.components.project

import javafx.scene.Node
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import me.mazeika.freelancer.binder.admin.ProjectSnapshot
import me.mazeika.freelancer.binder.i18n.I18nService
import me.mazeika.freelancer.view.services.ColorService
import java.math.BigDecimal
import javax.inject.Inject

class ProjectCellComponents @Inject constructor(
    private val i18nService: I18nService,
    private val colorService: ColorService
) {
    fun createIndicator(project: ProjectSnapshot): Node = Circle(5.0).apply {
        fill = colorService.colors[project.colorIndex]
    }

    fun createText(project: ProjectSnapshot): Node = TextFlow(
        Text(project.name),
        Text("\n"),
        Text(project.client.name).apply {
            fill = Color.GRAY
        }
    )

    fun createSpacer(): Node = Pane().apply {
        HBox.setHgrow(this, Priority.ALWAYS)
    }

    fun createRateText(project: ProjectSnapshot): Node = Text().apply {
        text = i18nService.formatMoney(
            project.hourlyRate,
            project.currency
        ) + "/hr"
        isVisible = project.hourlyRate != BigDecimal.ZERO
    }
}
