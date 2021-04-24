plugins {
    id("org.openjfx.javafxplugin")
}

javafx {
    version = "12"
    modules = listOf("javafx.base")
}

dependencies {
    implementation("org.jetbrains.exposed:exposed-core:0.30.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.30.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.30.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.30.1")
    implementation("org.xerial:sqlite-jdbc:3.34.0")
}
