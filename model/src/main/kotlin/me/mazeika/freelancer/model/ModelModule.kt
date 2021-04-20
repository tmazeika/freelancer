package me.mazeika.freelancer.model

import com.google.inject.AbstractModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

class ModelModule : AbstractModule() {
    override fun configure() {
        bind(CoroutineScope::class.java).toInstance(MainScope())
    }
}
