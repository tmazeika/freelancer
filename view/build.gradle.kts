plugins {
    application
    id("org.openjfx.javafxplugin")
}

javafx {
    version = "15.0.1"
    modules = listOf("javafx.controls")
}

application {
    mainClass.set("me.mazeika.freelancer.view.FXApp")
}

dependencies {
    implementation(project(":binder"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.4.3")
}
