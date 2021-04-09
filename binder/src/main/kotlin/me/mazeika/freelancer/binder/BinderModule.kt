package me.mazeika.freelancer.binder

import com.google.inject.AbstractModule
import me.mazeika.freelancer.binder.i18n.I18nModule

class BinderModule : AbstractModule() {

    override fun configure() {
        install(I18nModule())
    }
}
