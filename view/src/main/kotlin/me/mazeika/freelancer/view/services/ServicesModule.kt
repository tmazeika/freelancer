package me.mazeika.freelancer.view.services

import com.google.inject.AbstractModule
import me.mazeika.freelancer.binder.services.DialogService

class ServicesModule : AbstractModule() {
    override fun configure() {
        bind(DialogService::class.java).to(FXDialogService::class.java)
    }
}
