import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.32" apply false
    id("org.openjfx.javafxplugin") version "0.0.9" apply false
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
    }

    group = "me.mazeika.freelancer"
    version = "0.1.0"

    repositories {
        jcenter()
    }

    dependencies {
        val implementation by configurations
        val testImplementation by configurations
        val testRuntimeOnly by configurations

        implementation("com.google.inject:guice:5.0.1")
        implementation("com.google.inject.extensions:guice-assistedinject:5.0.1")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")

        testImplementation(kotlin("test-junit5"))
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "15"
            useIR = true
        }
    }
}
