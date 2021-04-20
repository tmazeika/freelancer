package me.mazeika.freelancer.view

import com.google.inject.Guice
import javafx.application.Application
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import me.mazeika.freelancer.binder.BinderModule

private val INJECTOR = Guice.createInjector(ViewModule(), BinderModule())

fun main(args: Array<String>) = Application.launch(FXApp::class.java, *args)

class FXApp : Application() {
    private val view = INJECTOR.getInstance(Parent::class.java)

    override fun start(stage: Stage) = with(stage) {
        Thread.setDefaultUncaughtExceptionHandler { _, cause ->
            cause.printStackTrace()
        }
        title = "Freelancer"
        scene = Scene(view, 800.0, 600.0).apply {
            stylesheets += FXApp::class.java.getResource("/styles.css")!!
                .toString()
        }
        show()
    }

    override fun stop() {
        INJECTOR.getInstance(CoroutineScope::class.java).cancel("Shutting down")
    }
}
