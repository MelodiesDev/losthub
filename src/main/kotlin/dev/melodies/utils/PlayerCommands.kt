package dev.melodies.utils

import dev.melodies.losthub.LostHubPlugins
import dev.melodies.lostmenu.MenuListener
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import org.incendo.cloud.annotations.Command

class PlayerCommands(private val plugin: LostHubPlugins) {
    @Command("cubes")
    fun cubes(sender: Player) {
        MenuListener.openCubeMenu(sender)
    }

    @Command("lobby")
    fun lobby(sender: Player) {
        if (sender.hasPermission("melodies.command.server")) {
            PlayerServerUtils.transfer(plugin, sender, "lobby")
            sender.sendMessage("<green>Transferring you now!</green>".toMiniMessage())
        } else
            sender.sendMessage("<red>You do not have permission to use this command.</red>".toMiniMessage())
    }

    @Command("info")
    fun info(sender: Player) {
        if (sender.hasPermission("melodies.command.infobook")) {
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
        } else
            sender.sendMessage("You do not have permission to use this command.")
    }

    @Command("shop")
    fun shop(sender: Player) {
        if (sender.hasPermission("melodies.command.shop")) {
            sender.sendMessage(
                "<gradient:dark_purple:light_purple>melodies.dev uber sale 100% off!!!</gradient>".toMiniMessage()
                    .clickEvent(
                        ClickEvent.openUrl("https://melodies.dev")
                    )
            )
        }
    }
}