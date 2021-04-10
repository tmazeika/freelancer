package me.mazeika.freelancer.view.components

import com.google.inject.AbstractModule
import com.google.inject.assistedinject.FactoryModuleBuilder

class ComponentsModule : AbstractModule() {

    override fun configure() {
        install(
            FactoryModuleBuilder()
                .implement(ProjectCell::class.java, ProjectCell::class.java)
                .implement(
                    CompactProjectCell::class.java,
                    CompactProjectCell::class.java
                )
                .build(ProjectCellFactory::class.java)
        )
    }
}
