package me.mazeika.freelancer.view.admin

import javafx.geometry.Pos
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import me.mazeika.freelancer.binder.admin.LineItemsAdminBinder
import me.mazeika.freelancer.binder.i18n.I18nService
import me.mazeika.freelancer.view.components.GraphicCellFactory
import me.mazeika.freelancer.view.components.TextCellFactory
import me.mazeika.freelancer.view.components.forms.*
import me.mazeika.freelancer.view.components.project.ProjectCellFactory
import javax.inject.Inject

class LineItemsAdminView @Inject constructor(
    vm: LineItemsAdminBinder,
    i18nService: I18nService,
    projectCellFactory: ProjectCellFactory
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
                    createCell = { TextCellFactory.useToString(it) }
                ),
                DateTimeFormComponent(
                    label = "Start",
                    value = lineItem.start,
                    zone = i18nService.defaultZone,
                    locale = i18nService.defaultLocale,
                ),
                DateTimeFormComponent(
                    label = "End",
                    value = lineItem.end,
                    zone = i18nService.defaultZone,
                    locale = i18nService.defaultLocale,
                ).apply {
                    lineItem.isEndEmpty.bind(emptyProperty)
                },
            )
        }
        center = AdminEntityList(vm) {
            GraphicCellFactory { lineItem ->
                HBox().apply {
                    alignment = Pos.CENTER_LEFT
                    spacing = 10.0
                    children.setAll(
                        TextFlow(
                            Text(lineItem.name),
                            Text("\n"),
                            Text("${lineItem.project.name} (${lineItem.project.client.name})").apply {
                                fill = Color.GRAY
                            }
                        ),
                        Pane().apply {
                            HBox.setHgrow(this, Priority.ALWAYS)
                        },
                        Text(i18nService.formatTime(lineItem.start))
                    )
                }
            }
        }
    }
}

