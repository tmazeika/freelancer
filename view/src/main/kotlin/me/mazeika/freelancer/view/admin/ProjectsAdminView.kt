package me.mazeika.freelancer.view.admin

import javafx.beans.binding.Bindings
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.shape.Circle
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import me.mazeika.freelancer.binder.admin.ProjectsAdminBinder
import me.mazeika.freelancer.binder.i18n.I18nService
import me.mazeika.freelancer.view.components.EntityForm
import me.mazeika.freelancer.view.services.ColorService
import me.mazeika.freelancer.view.util.BidiBindings
import me.mazeika.freelancer.view.util.ColorIndexBidiConverter
import java.math.BigDecimal
import javax.inject.Inject

class ProjectsAdminView @Inject constructor(
    vm: ProjectsAdminBinder,
    private val colorService: ColorService,
    private val i18nService: I18nService
) :
    EntityAdminView<ProjectsAdminBinder.ProjectBinder, ProjectsAdminBinder.FilledProjectBinder>(
        vm
    ) {
    override fun createEntityView(vm: ProjectsAdminBinder.ProjectBinder): Node =
        EntityForm(
            EntityForm.ComboInput(
                name = "Client",
                value = vm.clientName,
                options = vm.clientNames
            ),
            EntityForm.TextInput(
                name = "Name",
                value = vm.name,
                maxLength = vm.maxNameLength,
            ),
            EntityForm.ColorComboInput(
                name = "Color",
                value = BidiBindings.createProperty(
                    vm.colorIndex,
                    ColorIndexBidiConverter(colorService.colors)
                ),
                options = colorService.colors,
            ),
            EntityForm.NonNegativeDecimalInput(
                name = "Hourly Rate",
                value = vm.hourlyRate
            ),
            EntityForm.ComboInput(
                name = "Currency",
                value = vm.currency,
                options = i18nService.availableCurrencies,
            )
        )

    override fun createListCell(item: ProjectsAdminBinder.ProjectBinder): Node =
        HBox().apply {
            alignment = Pos.CENTER_LEFT
            spacing = 10.0
            val circle = Circle(5.0).apply {
                fillProperty().bind(
                    Bindings.createObjectBinding(
                        { colorService.colors[item.colorIndex.value] },
                        item.colorIndex
                    )
                )
            }
            val text = TextFlow(
                Text().apply {
                   textProperty().bind(item.name)
                },
                Text("\n"),
                Text().apply {
                    styleClass += "muted-text"
                    textProperty().bind(item.clientName)
                }
            )
            val spacer = Pane()
            HBox.setHgrow(spacer, Priority.ALWAYS)
            val rateText = Text().apply {
                visibleProperty().bind(Bindings.createBooleanBinding({
                    item.hourlyRate.value != BigDecimal.ZERO
                }, item.hourlyRate))
                textProperty().bind(Bindings.createStringBinding({
                    val hourlyRate = item.hourlyRate.value
                    val currency = item.currency.value
                    i18nService.formatMoney(hourlyRate, currency) + "/hr"
                }, item.hourlyRate, item.currency))
            }
            children.addAll(circle, text, spacer, rateText)
        }
}

