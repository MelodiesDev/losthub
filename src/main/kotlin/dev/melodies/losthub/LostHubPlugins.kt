package dev.melodies.losthub

import dev.melodies.losthubfeats.PlayerAFKItemDisplayEntity
import dev.melodies.losthubfeats.PlayerDoubleJump
import dev.melodies.lostmenu.CompassGrantListener
import dev.melodies.lostmenu.OpenNavigatorListener
import dev.melodies.lostmenu.OpenShopListener
import dev.melodies.lostmenu.ShopGrantListener
import dev.melodies.utils.PlayerServerUtils
import dev.melodies.utils.toMiniMessage
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.bukkit.CloudBukkitCapabilities
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.paper.PaperCommandManager


@Suppress("unused")
class LostHubPlugins : JavaPlugin() {
    override fun onEnable() {
        logger.info("LostHub has been enabled!")
        Bukkit.getPluginManager().registerEvents(CompassGrantListener(), this)
        Bukkit.getPluginManager().registerEvents(OpenNavigatorListener(this), this)
        Bukkit.getPluginManager().registerEvents(ShopGrantListener(), this)
        Bukkit.getPluginManager().registerEvents(OpenShopListener(), this)
        Bukkit.getPluginManager().registerEvents(PlayerDoubleJump(this), this)

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord")

        PlayerAFKItemDisplayEntity(this).init()

        val commandManager = PaperCommandManager.createNative(
            this, ExecutionCoordinator.asyncCoordinator()
        )

        if (commandManager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
            commandManager.registerBrigadier()
        } else if (commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            commandManager.registerAsynchronousCompletions()
        }

        val annotationParser = AnnotationParser(commandManager, CommandSender::class.java)
        annotationParser.parse(this)
    }

    @Command("lobby")
    fun lobby(sender: Player) {
        if (sender.hasPermission("melodies.command.server")) {
            PlayerServerUtils.transfer(this, sender, "lobby")
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

    @Command("gmc")
    fun creative(sender: Player) {
        sender.gameMode = GameMode.CREATIVE
    }

    @Command("gms")
    fun survival(sender: Player) {
        sender.gameMode = GameMode.SURVIVAL
    }

    @Command("gma")
    fun adventure(sender: Player) {
        sender.gameMode = GameMode.ADVENTURE
    }
    @Command("vanish")
    fun vanish(sender: Player) {
        sender.isInvisible = !sender.isInvisible
    }
}