package dev.melodies.utils.player

import com.google.common.io.ByteStreams
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin


object ServerTransferUtils {
    fun transfer(plugin: Plugin, player: Player, server: String) {
        val out = ByteStreams.newDataOutput()
        out.writeUTF("Connect")
        out.writeUTF(server)

        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray())
    }
}