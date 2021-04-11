package me.mazeika.freelancer.view.components.project

import com.google.inject.AbstractModule
import com.google.inject.assistedinject.FactoryModuleBuilder
import com.google.inject.name.Names
import javafx.scene.Node

class ProjectModule : AbstractModule() {
    override fun configure() {
        install(
            FactoryModuleBuilder()
                .implement(
                    Node::class.java,
                    Names.named("long"),
                    LongProjectCell::class.java
                )
                .implement(
                    Node::class.java,
                    Names.named("short"),
                    ShortProjectCell::class.java
                )
                .implement(
                    Node::class.java,
                    Names.named("button"),
                    ButtonProjectCell::class.java
                )
                .build(ProjectCellFactory::class.java)
        )
    }
}
