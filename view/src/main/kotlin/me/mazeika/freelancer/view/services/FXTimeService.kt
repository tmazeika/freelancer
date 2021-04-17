package me.mazeika.freelancer.view.services

import javafx.animation.AnimationTimer
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import java.time.Instant
import javax.inject.Singleton

@Singleton
class FXTimeService : AnimationTimer(), TimeService {

    override val nowProperty: ObjectProperty<Instant> =
        SimpleObjectProperty(Instant.now())

    private var lastSecond = nowProperty.value.epochSecond

    init {
        start()
    }

    override fun handle(ns: Long) {
        val now = Instant.now()
        if (now.epochSecond == lastSecond) {
            return
        }
        nowProperty.value = now
        lastSecond = now.epochSecond
    }
}
