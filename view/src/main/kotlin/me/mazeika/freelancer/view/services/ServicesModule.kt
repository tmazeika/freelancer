package me.mazeika.freelancer.view.services

import com.google.inject.AbstractModule
import me.mazeika.freelancer.binder.services.DialogService

class ServicesModule : AbstractModule() {

    override fun configure() {
        bind(ColorService::class.java).to(LightColorService::class.java)
        bind(DialogService::class.java).to(FXDialogService::class.java)
        bind(TimeService::class.java).to(FXTimeService::class.java)
    }
}
