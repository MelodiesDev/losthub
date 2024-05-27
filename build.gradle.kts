import dev.melodies.gradle.gitVersion
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import xyz.jpenilla.runpaper.task.RunServer

plugins {
    idea
    `maven-publish`
    id("dev.melodies.gradle.plugin")

    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.shadow)

    alias(libs.plugins.paperweight)
    alias(libs.plugins.plugin.yml)
    alias(libs.plugins.run.paper)
}

group = "dev.melodies"
version = rootProject.gitVersion

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public")
    maven("https://jitpack.io")
    maven("https://repo.xenondevs.xyz/releases")
}

dependencies {
    "paperweightDevelopmentBundle"(libs.paper.devbundle)

    api("xyz.xenondevs.invui:invui:1.25")
    api("xyz.xenondevs.invui:invui-kotlin:1.25")

    api("org.incendo:cloud-paper:2.0.0-beta.7")
    api("org.incendo:cloud-kotlin-coroutines-annotations:2.0.0-rc.1")
    api("org.incendo:cloud-annotations:2.0.0-rc.1")
}

configure<BukkitPluginDescription> {
    name = "LostHub"
    main = "dev.melodies.losthub.LostHubPlugins"

    authors = listOf("Melody <3")
    apiVersion = "1.20"

    commands {
        create("info") {
            description = "Opens the info book"
            permission = "losthub.info"
        }
        create("hub") {
            description = "Teleports you to the hub"
            permission = "losthub.server"
        }
    }
}

configure<KotlinJvmProjectExtension> {
    jvmToolchain(17)
    compilerOptions {
        // Store Java 8 parameter names
        javaParameters.set(true)
    }
}

tasks.withType<Tar> { enabled = false }
tasks.withType<Zip> { duplicatesStrategy = DuplicatesStrategy.EXCLUDE }
tasks.withType<Test> { useJUnitPlatform() }

tasks.withType<RunServer> {
    // Don't prevent the server from starting if the Java version is not supported
    systemProperty("Paper.IgnoreJavaVersion", "true")
    // Accept the EULA without using the eula.txt file
    systemProperty("com.mojang.eula.agree", "true")
    // Disable legacy formatting warnings
    systemProperty("net.kyori.adventure.text.warnWhenLegacyFormattingDetected", "false")
}