package dev.melodies.commands

import dev.melodies.utils.PlayerServerUtils
import dev.melodies.utils.toMiniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class ServerCommands(private val plugin: Plugin) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender as? Player ?: return false
        if (sender.hasPermission("melodies.command.server")) {
            PlayerServerUtils.transfer(plugin, player, "lobby")
            sender.sendMessage("<green>Transferring you now!</green>".toMiniMessage())
        } else
            sender.sendMessage("<red>You do not have permission to use this command.</red>".toMiniMessage())
        return false
    }
}