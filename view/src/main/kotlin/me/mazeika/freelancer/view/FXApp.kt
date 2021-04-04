package me.mazeika.freelancer.view

import com.google.inject.Guice
import javafx.application.Application
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

private val INJECTOR = Guice.createInjector(ViewModule())

fun main(args: Array<String>) = Application.launch(FXApp::class.java, *args)

class FXApp : Application() {
    private val view = INJECTOR.getInstance(Parent::class.java)

    override fun start(stage: Stage): Unit = with(stage) {
        Thread.setDefaultUncaughtExceptionHandler { _, cause ->
            cause.printStackTrace()
        }
        title = "Freelancer"
        scene = Scene(view, 800.0, 600.0)
        show()
    }
}
