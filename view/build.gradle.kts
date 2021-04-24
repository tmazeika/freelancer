plugins {
    application
    id("org.openjfx.javafxplugin")
}

javafx {
    version = "12"
    modules = listOf("javafx.controls")
}

application {
    mainClass.set("me.mazeika.freelancer.view.FXApp")
}

dependencies {
    implementation(project(":binder"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.4.3")
    runtimeOnly("org.slf4j:slf4j-jdk14:1.7.30")
}
