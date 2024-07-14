package dev.melodies.messages

import dev.melodies.utils.toMiniMessage
import net.kyori.adventure.title.Title
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class OnJoinMOTD : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        event.player.showTitle(Title.title("<gradient:aqua:dark_purple>LostOasis</gradient>".toMiniMessage(), "<green>Welcome!".toMiniMessage()))
    }
}