package me.mazeika.freelancer.view.admin

import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javax.inject.Inject

class AdminView @Inject constructor(
    projectsAdminView: ProjectsAdminView,
    tagsAdminView: TagsAdminView
) : TabPane() {
    init {
        tabClosingPolicy = TabClosingPolicy.UNAVAILABLE
        tabs += Tab("Clients")
        tabs += Tab("Projects", projectsAdminView)
        tabs += Tab("Tags", tagsAdminView)
    }
}
