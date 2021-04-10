package me.mazeika.freelancer.view.admin

import javafx.beans.property.SimpleObjectProperty
import javafx.scene.layout.BorderPane
import me.mazeika.freelancer.binder.admin.ProjectsAdminBinder
import me.mazeika.freelancer.binder.i18n.I18nService
import me.mazeika.freelancer.view.components.GraphicCellFactory
import me.mazeika.freelancer.view.components.TextCellFactory
import me.mazeika.freelancer.view.components.forms.*
import me.mazeika.freelancer.view.components.project.ProjectCellFactory
import me.mazeika.freelancer.view.services.ColorService
import me.mazeika.freelancer.view.util.BidiBindings
import me.mazeika.freelancer.view.util.ColorIndexConverter
import javax.inject.Inject

class ProjectsAdminView @Inject constructor(
    vm: ProjectsAdminBinder,
    private val colorService: ColorService,
    private val i18nService: I18nService,
    private val projectCellFactory: ProjectCellFactory,
) : BorderPane() {

    init {
        top = AdminActionBar(vm) { project ->
            GridForm(
                OptionsFormComponent(
                    label = "Client",
                    value = project.client,
                    options = vm.allClients,
                    createCell = { TextCellFactory { it.name.value } }
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
            GraphicCellFactory(projectCellFactory::createLong)
        }
    }
}

