package dev.melodies.losthub

import dev.melodies.gadgets.TrampolineItem
import dev.melodies.actions.PlayerAFKParticleDisplay
import dev.melodies.actions.PlayerDoubleJump
import dev.melodies.actions.VaultOpener
import dev.melodies.gadgets.RepulsionBowItem
import dev.melodies.messages.OnJoinMOTD
import dev.melodies.utils.MenuListener
import dev.melodies.utils.commands.AdminCommands
import dev.melodies.utils.commands.PlayerCommands
import dev.melodies.utils.player.PlayerJoinItemGrantListener
import dev.melodies.utils.ScoreboardEnabler
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.bukkit.CloudBukkitCapabilities
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.paper.LegacyPaperCommandManager


class LostHubPlugins : JavaPlugin() {
    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(PlayerDoubleJump(this), this)
        Bukkit.getPluginManager().registerEvents(PlayerJoinItemGrantListener(), this)
        Bukkit.getPluginManager().registerEvents(OnJoinMOTD(), this)
        Bukkit.getPluginManager().registerEvents(MenuListener(this), this) // Opens the menu

        Bukkit.getPluginManager().registerEvents(TrampolineItem(this), this) // Opens the menu
        Bukkit.getPluginManager().registerEvents(RepulsionBowItem(this), this) // Opens the menu

        val enabler = ScoreboardEnabler(this)
        enabler.scheduleScoreboardUpdates()
        Bukkit.getPluginManager().registerEvents(enabler, this)

        val vaultOpener = VaultOpener()
        Bukkit.getPluginManager().registerEvents(vaultOpener, this)

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
