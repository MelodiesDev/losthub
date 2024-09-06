package dev.melodies.utils.commands

import dev.melodies.actions.VaultOpener
import dev.melodies.losthub.LostHubPlugins
import dev.melodies.utils.TimeFormatting
import dev.melodies.utils.player.PlayerDataStorage
import dev.melodies.utils.player.ServerTransferUtils
import dev.melodies.utils.toMiniMessage
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import org.incendo.cloud.annotations.Command
import kotlin.time.Duration.Companion.days

class PlayerCommands(private val plugin: LostHubPlugins) {

    @Command("lobby")
    fun lobby(sender: Player) {
        ServerTransferUtils.transfer(plugin, sender, "lobby")
            sender.sendMessage("<green>Transferring you now!</green>".toMiniMessage())
    }

    @Command("info")
    fun info(sender: Player) {
            val book = ItemStack(Material.WRITTEN_BOOK)
            book.editMeta(BookMeta::class.java) { meta ->
                meta.title(MiniMessage.miniMessage().deserialize("<gradient:blue:aqua>Info</gradient>"))
                meta.author(MiniMessage.miniMessage().deserialize("<gradient:blue:aqua>Server</gradient>"))
                meta.addPages(
                    MiniMessage.miniMessage().deserialize(
                        """
                        <b><dark_purple>Enchants Vol 1</dark_purple></b>
                        
                        <gradient:blue:aqua>Efficiency</gradient> - Increases mining speed exponentially increasing with level.
                        
                        <gradient:blue:aqua>Fortune</gradient> - Increases block drops multiplier raises with each level.

                        """.trimIndent()
                    ),
                    MiniMessage.miniMessage().deserialize(
                        """
                        <b><dark_purple>Enchants Vol 2</dark_purple></b>
                        
                        <gradient:blue:aqua>Explosive</gradient> - Chance to explode blocks in a larger radius each level.
                        
                        <gradient:blue:aqua>Meteor Rain</gradient> - Summons meteors to rain down increasing in quantity each level.

                        """.trimIndent()
                    ),
                    MiniMessage.miniMessage().deserialize(
                        """
                        <b><dark_purple>Enchants Vol 3</dark_purple></b>

                        <gradient:blue:aqua>Lightning Storm</gradient> - Summons a lightning storm that breaks blocks randomly.

                        """.trimIndent()
                    )
                )
            }
            sender.openBook(book)
    }

    @Command("shop")
    fun shop(sender: Player) {
            sender.sendMessage(
                "<gradient:dark_purple:light_purple>melodies.dev uber sale 100% off!!!</gradient>".toMiniMessage()
                    .clickEvent(
                        ClickEvent.openUrl("https://melodies.dev")
                    )
            )
    }

    @Command("daily")
    fun daily(sender: Player) {
        val now = System.currentTimeMillis()
        val playerUUID = sender.uniqueId
        val lastUsage = CommandCooldownStorage.getCooldown(playerUUID)
        val timeSinceLastUsage = now - (lastUsage ?: 0)
        val countdown = 1.days.inWholeMilliseconds - timeSinceLastUsage

        if (countdown > 0) {
            val formatted = TimeFormatting.formatRemainingDuration(lastUsage ?: 0, 1.days.inWholeMilliseconds)
            sender.sendMessage("<red>You can collect your daily reward again in <gold>$formatted</gold>.</red>".toMiniMessage())
            sender.playSound(sender.location, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.5f)
            return
        }

        PlayerDataStorage.setPlayerStats(sender.uniqueId.toString(), +10, +100)
            sender.sendMessage("<green>You collected your daily +10 <gold>coins</gold> and +10 <aqua>xp</aqua>!</green>".toMiniMessage())
            sender.playSound(sender.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)

        CommandCooldownStorage.setCooldown(playerUUID, now)
    }

    @Command("vault")
    fun vault(sender: Player) {
        VaultOpener.openVault(sender)
    }
}