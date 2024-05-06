rootProject.name = "lost-hub"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

includeBuild("gradle-plugin")