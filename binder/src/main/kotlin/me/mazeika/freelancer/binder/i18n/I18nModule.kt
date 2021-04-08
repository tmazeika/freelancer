package me.mazeika.freelancer.binder.i18n

import com.google.inject.AbstractModule

class I18nModule : AbstractModule() {
    override fun configure() {
        bind(I18nService::class.java).to(LocalI18nService::class.java)
    }
}
