package me.mazeika.freelancer.view.admin

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import javafx.scene.text.TextFlow
import me.mazeika.freelancer.binder.admin.LineItemsAdminBinder
import me.mazeika.freelancer.binder.admin.TimeLineItemSnapshot
import me.mazeika.freelancer.binder.i18n.I18nService
import me.mazeika.freelancer.binder.util.map
import me.mazeika.freelancer.view.components.GraphicCellFactory
import me.mazeika.freelancer.view.components.TagDisplay
import me.mazeika.freelancer.view.components.TextCellFactory
import me.mazeika.freelancer.view.components.forms.*
import me.mazeika.freelancer.view.components.project.ProjectCellFactory
import me.mazeika.freelancer.view.services.TimeService
import javax.inject.Inject

class LineItemsAdminView @Inject constructor(
    private val vm: LineItemsAdminBinder,
    private val i18nService: I18nService,
    private val timeService: TimeService,
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
                BorderPane().apply {
                    padding = Insets(5.0)
                    left = TextFlow(
                        Text(lineItem.name),
                        Text("\n"),
                        Text("${lineItem.project.name} (${lineItem.project.client.name})").apply {
                            fill = Color.GRAY
                        }
                    ).apply {
                        lineSpacing = 5.0
                    }
                    if (lineItem.tags.isNotEmpty()) {
                        bottom = FlowPane().apply {
                            padding = Insets(10.0, 0.0, 0.0, 0.0)
                            hgap = 5.0
                            vgap = 5.0
                            children.setAll(lineItem.tags.map(::TagDisplay))
                        }
                    }
                    val timeInfo = if (lineItem.end == null) {
                        RunningTimeText(lineItem)
                    } else {
                        StoppedTimeText(lineItem)
                    }.also {
                        it.visibleProperty().bind(hoverProperty().not())
                    }
                    val actionBtn = if (lineItem.end == null) {
                        Button("Stop").apply {
                            setOnAction { vm.onStop(lineItem) }
                        }
                    } else {
                        Button("Resume").apply {
                            setOnAction { vm.onResume(lineItem) }
                        }
                    }.also {
                        it.visibleProperty().bind(hoverProperty())
                    }
                    right = StackPane(timeInfo, actionBtn).apply {
                        alignment = Pos.CENTER_RIGHT
                    }
                }
            }
        }
    }

    private inner class RunningTimeText(lineItem: TimeLineItemSnapshot) :
        TextFlow(
            Text().apply {
                fill = Color.DODGERBLUE
                textProperty().bind(timeService.nowProperty.map { now ->
                    i18nService.formatDuration(lineItem.start, now)
                })
            },
            Text("\n"),
            Text("Started ${i18nService.formatLongTime(lineItem.start)}").apply {
                fill = Color.GRAY
                minWidth = USE_PREF_SIZE
            },
        ) {
        init {
            textAlignment = TextAlignment.RIGHT
            lineSpacing = 5.0
        }
    }

    private inner class StoppedTimeText(lineItem: TimeLineItemSnapshot) :
        TextFlow(
            Text(
                i18nService.formatDuration(
                    lineItem.start,
                    lineItem.end!!
                )
            ),
            Text("\n"),
            Text("Started ${i18nService.formatLongTime(lineItem.start)}").apply {
                fill = Color.GRAY
            }
        ) {
        init {
            textAlignment = TextAlignment.RIGHT
            lineSpacing = 5.0
        }
    }
}

