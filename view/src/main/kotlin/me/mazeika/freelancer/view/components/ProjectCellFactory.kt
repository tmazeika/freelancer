package me.mazeika.freelancer.view.components

import me.mazeika.freelancer.binder.admin.ProjectBinder

interface ProjectCellFactory {

    fun create(project: ProjectBinder): ProjectCell

    fun createCompact(project: ProjectBinder): CompactProjectCell
}
