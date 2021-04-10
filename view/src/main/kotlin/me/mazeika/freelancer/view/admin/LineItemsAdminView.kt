package me.mazeika.freelancer.view.admin

import javafx.scene.layout.BorderPane
import me.mazeika.freelancer.binder.admin.LineItemsAdminBinder
import me.mazeika.freelancer.view.components.GraphicCellFactory
import me.mazeika.freelancer.view.components.TextCellFactory
import me.mazeika.freelancer.view.components.forms.GridForm
import me.mazeika.freelancer.view.components.forms.MultiOptionsFormComponent
import me.mazeika.freelancer.view.components.forms.OptionsFormComponent
import me.mazeika.freelancer.view.components.forms.TextFormComponent
import me.mazeika.freelancer.view.components.project.ProjectCellFactory
import javax.inject.Inject

class LineItemsAdminView @Inject constructor(
    vm: LineItemsAdminBinder,
    private val projectCellFactory: ProjectCellFactory
) : BorderPane() {

    init {
        top = AdminActionBar(vm) { lineItem ->
            GridForm(
                OptionsFormComponent(
                    label = "Project",
                    value = lineItem.project,
                    options = vm.allProjects,
                    createCell = {
                        GraphicCellFactory(projectCellFactory::createShort)
                    },
                    createButtonCell = {
                        GraphicCellFactory(projectCellFactory::createButton)
                    }
                ),
                TextFormComponent(
                    label = "Name",
                    value = lineItem.name,
                    maxLength = lineItem.maxNameLength
                ),
                MultiOptionsFormComponent(
                    label = "Tags",
                    values = lineItem.tags,
                    options = vm.allTags,
                    createCell = { TextCellFactory { it.name.value } }
                )
            )
        }
        center = AdminEntityList(vm) { TextCellFactory { it.name.value } }
    }
}

