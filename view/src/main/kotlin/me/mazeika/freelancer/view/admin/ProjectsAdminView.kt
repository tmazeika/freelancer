package me.mazeika.freelancer.view.admin

import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Pos
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.shape.Circle
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import me.mazeika.freelancer.binder.admin.ProjectsAdminBinder
import me.mazeika.freelancer.binder.i18n.I18nService
import me.mazeika.freelancer.binder.util.bind
import me.mazeika.freelancer.binder.util.map
import me.mazeika.freelancer.view.components.GraphicCellFactory
import me.mazeika.freelancer.view.components.forms.*
import me.mazeika.freelancer.view.services.ColorService
import me.mazeika.freelancer.view.util.BidiBindings
import me.mazeika.freelancer.view.util.ColorIndexConverter
import java.math.BigDecimal
import javax.inject.Inject

class ProjectsAdminView @Inject constructor(
    vm: ProjectsAdminBinder,
    private val colorService: ColorService,
    private val i18nService: I18nService
) : BorderPane() {

    init {
        top = AdminActionBar(vm) { project ->
            GridForm(
                OptionsFormComponent(
                    label = "Client",
                    value = project.clientName,
                    options = project.clientNames.toList()
                ),
                TextFormComponent(
                    label = "Name",
                    value = project.name,
                    maxLength = project.maxNameLength
                ),
                ColorOptionsFormComponent(
                    label = "Color",
                    value = BidiBindings.bind(
                        SimpleObjectProperty(),
                        project.colorIndex,
                        ColorIndexConverter(colorService.colors)
                    ),
                    options = colorService.colors
                ),
                NonNegativeDecimalFormComponent(
                    label = "Hourly Rate",
                    value = project.hourlyRate
                ),
                OptionsFormComponent(
                    label = "Currency",
                    value = project.currency,
                    options = i18nService.availableCurrencies
                )
            )
        }
        center = AdminEntityList(vm) {
            GraphicCellFactory { project ->
                HBox().apply {
                    alignment = Pos.CENTER_LEFT
                    spacing = 10.0
                    val rateText = bind({ hourlyRate, currency ->
                        i18nService.formatMoney(hourlyRate, currency) + "/hr"
                    }, project.hourlyRate, project.currency)
                    children.setAll(
                        Circle(5.0).apply {
                            fillProperty().bind(project.colorIndex.map { i ->
                                colorService.colors[i.toInt()]
                            })
                        },
                        TextFlow(
                            Text().apply {
                                textProperty().bind(project.name)
                            },
                            Text("\n"),
                            Text().apply {
                                styleClass += "muted-text"
                                textProperty().bind(project.clientName)
                            }
                        ),
                        Pane().apply {
                            HBox.setHgrow(this, Priority.ALWAYS)
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
        }
    }
}

