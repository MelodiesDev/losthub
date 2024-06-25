package dev.melodies.losthub

import dev.melodies.gadgets.TrampolineItem
import dev.melodies.losthubfeats.PlayerAFKParticleDisplay
import dev.melodies.losthubfeats.PlayerDoubleJump
import dev.melodies.lostmenu.MenuListener
import dev.melodies.utils.*
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.bukkit.CloudBukkitCapabilities
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.paper.LegacyPaperCommandManager


@Suppress("unused")
class LostHubPlugins : JavaPlugin() {
    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(PlayerDoubleJump(this), this)
        Bukkit.getPluginManager().registerEvents(PlayerJoinItemGrantListener(), this)
        Bukkit.getPluginManager().registerEvents(MenuListener(this), this) // Opens the menu
        Bukkit.getPluginManager().registerEvents(TrampolineItem(this), this) // Opens the menu

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord")

        PlayerAFKParticleDisplay(this).init()

        val commandManager = LegacyPaperCommandManager.createNative(
            this, ExecutionCoordinator.simpleCoordinator()
        )

        if (commandManager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
            commandManager.registerBrigadier()
        } else if (commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            commandManager.registerAsynchronousCompletions()
        }

        val annotationParser = AnnotationParser(commandManager, CommandSender::class.java)
        annotationParser.parse(this, PlayerCommands(this), AdminCommands(this))

        logger.info("LostHub has been enabled!")
    }
}
