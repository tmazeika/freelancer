plugins {
    id("org.openjfx.javafxplugin")
}

javafx {
    version = "15.0.1"
    modules = listOf("javafx.graphics")
}

dependencies {
    implementation(project(":model"))
}
