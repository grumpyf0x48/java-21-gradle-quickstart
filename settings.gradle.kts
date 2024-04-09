pluginManagement {
    val nativeImagePluginId: String by settings
    val nativeImagePluginVersion: String by settings

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        id(nativeImagePluginId) version nativeImagePluginVersion
    }
}

rootProject.name = "gradle_quickstart"
