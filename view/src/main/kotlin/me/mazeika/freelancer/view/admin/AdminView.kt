package me.mazeika.freelancer.view.admin

import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javax.inject.Inject

class AdminView @Inject constructor(
    clientsAdminView: ClientsAdminView,
    projectsAdminView: ProjectsAdminView,
    tagsAdminView: TagsAdminView,
    lineItemsAdminView: LineItemsAdminView
) : TabPane() {
    init {
        tabClosingPolicy = TabClosingPolicy.UNAVAILABLE
        tabs += Tab("Clients", clientsAdminView)
        tabs += Tab("Projects", projectsAdminView)
        tabs += Tab("Tags", tagsAdminView)
        tabs += Tab("Line Items", lineItemsAdminView)
    }
}
