package me.mazeika.freelancer.binder

import com.google.inject.AbstractModule
import me.mazeika.freelancer.binder.i18n.I18nModule
import me.mazeika.freelancer.model.ModelModule

class BinderModule : AbstractModule() {
    override fun configure() {
        install(ModelModule())
        install(I18nModule())
    }
}
