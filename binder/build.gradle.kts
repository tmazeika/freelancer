plugins {
    id("org.openjfx.javafxplugin")
}

javafx {
    version = "12"
    modules = listOf("javafx.graphics")
}

dependencies {
    implementation(project(":model"))
}
