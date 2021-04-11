package me.mazeika.freelancer.view.components.project

import javafx.scene.Node
import me.mazeika.freelancer.binder.admin.ProjectSnapshot
import javax.inject.Named

interface ProjectCellFactory {
    @Named("long")
    fun createLong(project: ProjectSnapshot): Node

    @Named("short")
    fun createShort(project: ProjectSnapshot): Node

    @Named("button")
    fun createButton(project: ProjectSnapshot): Node
}
