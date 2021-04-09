package me.mazeika.freelancer.view

import javafx.scene.layout.BorderPane
import me.mazeika.freelancer.view.admin.AdminView
import javax.inject.Inject

class RootView @Inject constructor(adminView: AdminView) : BorderPane() {

    init {
        center = adminView
    }
}
