package me.mazeika.freelancer.view.components

import com.google.inject.AbstractModule
import me.mazeika.freelancer.view.components.project.ProjectModule

class ComponentsModule : AbstractModule() {
    override fun configure() {
        install(ProjectModule())
    }
}
