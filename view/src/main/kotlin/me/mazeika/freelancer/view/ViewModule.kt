package me.mazeika.freelancer.view

import com.google.inject.AbstractModule
import javafx.scene.Parent
import me.mazeika.freelancer.view.components.ComponentsModule
import me.mazeika.freelancer.view.services.ServicesModule

class ViewModule : AbstractModule() {

    override fun configure() {
        bind(Parent::class.java).to(RootView::class.java)
        install(ComponentsModule())
        install(ServicesModule())
    }
}
