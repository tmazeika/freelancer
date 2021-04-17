package me.mazeika.freelancer.view.admin

import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.layout.*
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
                HBox().apply {
                    val hoverProp = hoverProperty()
                    val resumeHoverProp = hoverProp
                        .and(SimpleBooleanProperty(lineItem.end != null))
                    val stopHoverProp = hoverProp
                        .and(SimpleBooleanProperty(lineItem.end == null))
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
                        FlowPane().apply {
                            HBox.setHgrow(this, Priority.NEVER)
                            minWidth = 0.0
                            prefWidth = 0.0
                            alignment = Pos.CENTER_LEFT
                            hgap = 10.0
                            children.setAll(lineItem.tags.map(::TagDisplay))
                        },
                        Pane().apply {
                            HBox.setHgrow(this, Priority.ALWAYS)
                            minWidth = 0.0
                            prefWidth = 0.0
                        },
                        Button("Resume").apply {
                            setOnAction { vm.onResume(lineItem) }
                            visibleProperty().bind(resumeHoverProp)
                            managedProperty().bind(resumeHoverProp)
                        },
                        Button("Stop").apply {
                            setOnAction { vm.onStop(lineItem) }
                            visibleProperty().bind(stopHoverProp)
                            managedProperty().bind(stopHoverProp)
                        },
                        if (lineItem.end == null) {
                            CurrentTimeText(lineItem)
                        } else {
                            DoneTimeText(lineItem)
                        }.apply {
                            visibleProperty().bind(hoverProp.not())
                            managedProperty().bind(hoverProp.not())
                        }
                    )
                }
            }
        }
    }

    private inner class CurrentTimeText(lineItem: TimeLineItemSnapshot) :
        TextFlow() {
        init {
            textAlignment = TextAlignment.RIGHT
            children.setAll(
                Text().apply {
                    fill = Color.DODGERBLUE
                    textProperty().bind(timeService.nowProperty.map { now ->
                        i18nService.formatDuration(lineItem.start, now)
                    })
                },
                Text("\n"),
                Text("Started ${i18nService.formatLongTime(lineItem.start)}").apply {
                    fill = Color.GRAY
                },
            )
        }
    }

    private inner class DoneTimeText(lineItem: TimeLineItemSnapshot) :
        TextFlow() {
        init {
            textAlignment = TextAlignment.RIGHT
            children.setAll(
                Text(
                    i18nService.formatDuration(
                        lineItem.start,
                        lineItem.end!!
                    )
                ),
                Text("\n"),
                Text("Started ${i18nService.formatLongTime(lineItem.start)}").apply {
                    fill = Color.GRAY
                },
            )
        }
    }
}

