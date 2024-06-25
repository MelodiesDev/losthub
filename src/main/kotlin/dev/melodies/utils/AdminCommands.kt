package dev.melodies.utils

import dev.melodies.losthub.LostHubPlugins
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Display
import org.bukkit.entity.Player
import org.bukkit.entity.TextDisplay
import org.incendo.cloud.annotation.specifier.Greedy
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command

class AdminCommands(private val plugin: LostHubPlugins) {
    @Command("gmc")
    fun creative(sender: Player) {
        if (sender.hasPermission("melodies.command.gmc")) {
            sender.gameMode = GameMode.CREATIVE
        } else
            sender.sendMessage("You do not have permission to use this command.")
    }

    @Command("gms")
    fun survival(sender: Player) {
        if (sender.hasPermission("melodies.command.gms")) {
            sender.gameMode = GameMode.SURVIVAL
        }
    }

    @Command("gma")
    fun adventure(sender: Player) {
        if (sender.hasPermission("melodies.command.gma")) {
            sender.gameMode = GameMode.ADVENTURE
        }
    }

    @Command("gmsp")
    fun spectator(sender: Player) {
        if (sender.hasPermission("melodies.command.gmsp")) {
            sender.gameMode = GameMode.SPECTATOR
        }
    }

    @Command("vanish")
    fun vanish(sender: Player) {
        sender.isInvisible = !sender.isInvisible
    }

    @Command("create hologram here <size> <fixed> <text>")
    fun createHologramPlayer(
        sender: Player,
        @Argument("fixed") fixed: Boolean,
        @Argument("size") size: Double,
        @Argument("text") @Greedy text: String
    ) {
        val hologram = sender.world.spawn(sender.location, TextDisplay::class.java)
        hologram.billboard = if (fixed) Display.Billboard.FIXED else Display.Billboard.CENTER
        hologram.transformation = hologram.transformation.apply { scale.set(size) }
        hologram.text(text.toMiniMessage())
    }

    @Command("create hologram at <x> <y> <z> <size> <fixed> <text>")
    fun createHologramLocation(
        sender: Player,
        @Argument("fixed") fixed: Boolean,
        @Argument("size") size: Double,
        @Argument("x") x: Double,
        @Argument("y") y: Double,
        @Argument("z") z: Double,
        @Argument("text") @Greedy text: String
    ) {
        val location = Location(sender.world, x, y, z)
        val hologram = sender.world.spawn(location, TextDisplay::class.java)
        hologram.billboard = if (fixed) Display.Billboard.FIXED else Display.Billboard.CENTER
        hologram.transformation = hologram.transformation.apply { scale.set(size) }
        hologram.text(text.toMiniMessage())
    }
}