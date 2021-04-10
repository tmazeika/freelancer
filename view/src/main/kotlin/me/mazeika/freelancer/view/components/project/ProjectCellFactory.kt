package me.mazeika.freelancer.view.components.project

import javafx.scene.Node
import me.mazeika.freelancer.binder.admin.ProjectBinder
import javax.inject.Named

interface ProjectCellFactory {

    @Named("long")
    fun createLong(project: ProjectBinder): Node

    @Named("short")
    fun createShort(project: ProjectBinder): Node

    @Named("button")
    fun createButton(project: ProjectBinder): Node
}
