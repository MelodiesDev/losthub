package dev.melodies.lostmenu

import dev.melodies.gadgets.TrampolineItem.Companion.TRAMPOLINE
import dev.melodies.losthub.LostHubPlugins
import dev.melodies.utils.PlayerJoinItemGrantListener
import dev.melodies.utils.PlayerServerUtils
import dev.melodies.utils.toMiniMessage
import dev.melodies.utils.wrapped
import net.kyori.adventure.text.event.ClickEvent
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem
import xyz.xenondevs.invui.window.Window

class MenuListener(private val plugin: LostHubPlugins) : Listener {
    @EventHandler
    fun menuChecker(event: PlayerInteractEvent) {
        val player = event.player

        if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return

        val item = event.item ?: return

        val keysMap = mapOf(
            PlayerJoinItemGrantListener.GADGETKEY to (item.itemMeta?.persistentDataContainer?.has(
                PlayerJoinItemGrantListener.GADGETKEY
            ) ?: false),
            PlayerJoinItemGrantListener.COMPASSKEY to (item.itemMeta?.persistentDataContainer?.has(
                PlayerJoinItemGrantListener.COMPASSKEY
            ) ?: false),
            PlayerJoinItemGrantListener.SHOPKEY to (item.itemMeta?.persistentDataContainer?.has(
                PlayerJoinItemGrantListener.SHOPKEY
            ) ?: false)
        )

        if (keysMap[PlayerJoinItemGrantListener.SHOPKEY] == true) {
            val gui = Gui.normal() // Creates the GuiBuilder for a normal GUI
                .setStructure(
                    ". . . . # . . . .",
                    ". . . . . . . . .",
                    ". . . . . . . . .",
                    ". . . . . . . . .",
                )
                .addIngredient(
                    '#', SimpleItem(
                        ItemBuilder(Material.NAME_TAG)
                            .setDisplayName(
                                "<gradient:blue:aqua>Open Shop</gradient>".toMiniMessage().wrapped()
                            )
                            .addLoreLines(
                                "<gradient:dark_purple:light_purple>Click to open the shop in your browser</gradient>".toMiniMessage()
                                    .wrapped()
                            )
                    )
                    {
                        event.player.closeInventory()
                        event.player.sendMessage(
                            "<gradient:dark_purple:light_purple>melodies.dev uber sale 100% off!!!</gradient>".toMiniMessage()
                                .clickEvent(
                                    ClickEvent.openUrl("https://melodies.dev")
                                )
                        )
                    }
                )
                .build()

            val window = Window.single()
                .setViewer(event.player)
                .setTitle("Directory")
                .setGui(gui)
                .build()

            window.open()

        }
        if (keysMap[PlayerJoinItemGrantListener.COMPASSKEY] == true) {
            val gui = Gui.normal() // Creates the GuiBuilder for a normal GUI
                .setStructure(
                    ". # . % . . . . .",
                )
                .addIngredient(
                    '#', SimpleItem(
                        ItemBuilder(Material.LAPIS_BLOCK)
                            .setDisplayName(
                                "<gradient:blue:aqua>Far Shore</gradient>".toMiniMessage().wrapped()
                            )
                            .addLoreLines(
                                "<gradient:dark_purple:light_purple>Click to enter to the Far Shore</gradient>".toMiniMessage()
                                    .wrapped()
                            )
                    ) {
                        it.player.playSound(it.player.location, "minecraft:block.end_portal.spawn", 1.0f, 1.0f)
                        it.player.spawnParticle(
                            org.bukkit.Particle.DRAGON_BREATH,
                            it.player.location,
                            100,
                            1.0,
                            0.0,
                            1.0,
                            0.2
                        )
                        PlayerServerUtils.transfer(plugin, it.player, "far_shore")
                    }
                )
                .addIngredient(
                    '%', SimpleItem(
                        ItemBuilder(Material.IRON_BLOCK)
                            .setDisplayName(
                                "<gradient:dark_gray:gray>Prison</gradient>".toMiniMessage().wrapped()
                            )
                            .addLoreLines(
                                "<gradient:dark_purple:light_purple>Click to enter Prison</gradient>".toMiniMessage()
                                    .wrapped()
                            )
                    ) {
                        it.player.world.playSound(
                            it.player.location,
                            "minecraft:entity.armadillo.scute_drop",
                            0.5f,
                            1.0f
                        )
                        it.player.world.spawnParticle(
                            org.bukkit.Particle.DUST_PLUME,
                            it.player.location,
                            100,
                            0.1,
                            0.8,
                            0.1,
                            0.01
                        )
                        PlayerServerUtils.transfer(plugin, it.player, "prison")
                    }
                )
                .build()

            val window = Window.single()
                .setViewer(event.player)
                .setTitle("Directory")
                .setGui(gui)
                .build()

            window.open()
        }

        if (keysMap[PlayerJoinItemGrantListener.GADGETKEY] == true) {
            val gui = Gui.normal() // Creates the GuiBuilder for a normal GUI
                .setStructure(
                    ". # . . . . . . .",
                )
                .addIngredient(
                    '#', SimpleItem(
                        ItemBuilder(TRAMPOLINE)
                    ) {
                        player.inventory.addItem(TRAMPOLINE)
                    }
                )
                .build()

            val window = Window.single()
                .setViewer(event.player)
                .setTitle("Gadgets")
                .setGui(gui)
                .build()

            window.open()

        }
    }

    companion object {
        fun openCubeMenu(sender: Player) {
            val gui = Gui.normal() // Creates the GuiBuilder for a normal GUI
                .setStructure(
                    "! @ # $ % ^ & * (",
                )
                .addIngredient(
                    '!', SimpleItem(
                        ItemBuilder(Material.CHEST)
                            .setDisplayName(
                                "<gradient:green:aqua>Earthly Cube</gradient>".toMiniMessage().wrapped()
                            )
                            .addLoreLines(
                                "<dark_red>Click to open an Earthly Cube!</dark_red>".toMiniMessage()
                                    .wrapped()
                            )
                    )
                )
                .addIngredient(
                    '@', SimpleItem(
                        ItemBuilder(Material.CHEST)
                            .setDisplayName(
                                "<gradient:green:aqua>Ghoulish Cube</gradient>".toMiniMessage().wrapped()
                            )
                            .addLoreLines(
                                "<dark_red>Click to open a Ghoulish Cube!</dark_red>".toMiniMessage()
                                    .wrapped()
                            )
                    )
                )
                .addIngredient(
                    '#', SimpleItem(
                        ItemBuilder(Material.CHEST)
                            .setDisplayName(
                                "<gradient:green:aqua>Spectral Cube</gradient>".toMiniMessage().wrapped()
                            )
                            .addLoreLines(
                                "<dark_red>Click to open an Spectral Cube!</dark_red>".toMiniMessage()
                                    .wrapped()
                            )
                    )
                )
                .addIngredient(
                    '$', SimpleItem(
                        ItemBuilder(Material.CHEST)
                            .setDisplayName(
                                "<gradient:green:aqua>Magical Cube</gradient>".toMiniMessage().wrapped()
                            )
                            .addLoreLines(
                                "<dark_red>Click to open an Magical Cube!</dark_red>".toMiniMessage()
                                    .wrapped()
                            )
                    )
                )
                .addIngredient(
                    '%', SimpleItem(
                        ItemBuilder(Material.CHEST)
                            .setDisplayName(
                                "<gradient:green:aqua>Phantasmal Cube</gradient>".toMiniMessage().wrapped()
                            )
                            .addLoreLines(
                                "<dark_red>Click to open an Phantasmal Cube!</dark_red>".toMiniMessage()
                                    .wrapped()
                            )
                    )
                )
                .addIngredient(
                    '^', SimpleItem(
                        ItemBuilder(Material.CHEST)
                            .setDisplayName(
                                "<gradient:green:aqua>Mystical Cube</gradient>".toMiniMessage().wrapped()
                            )
                            .addLoreLines(
                                "<dark_red>Click to open an Mystical Cube!</dark_red>".toMiniMessage()
                                    .wrapped()
                            )
                    )
                )
                .addIngredient(
                    '&', SimpleItem(
                        ItemBuilder(Material.CHEST)
                            .setDisplayName(
                                "<gradient:green:aqua>Mystical Cube</gradient>".toMiniMessage().wrapped()
                            )
                            .addLoreLines(
                                "<dark_red>Click to open an Mystical Cube!</dark_red>".toMiniMessage()
                                    .wrapped()
                            )
                    )
                )
                .addIngredient(
                    '*', SimpleItem(
                        ItemBuilder(Material.CHEST)
                            .setDisplayName(
                                "<gradient:green:aqua>Mystical Cube</gradient>".toMiniMessage().wrapped()
                            )
                            .addLoreLines(
                                "<dark_red>Click to open an Mystical Cube!</dark_red>".toMiniMessage()
                                    .wrapped()
                            )
                    )
                )
                .addIngredient(
                    '(', SimpleItem(
                        ItemBuilder(Material.CHEST)
                            .setDisplayName(
                                "<gradient:green:aqua>Mystical Cube</gradient>".toMiniMessage().wrapped()
                            )
                            .addLoreLines(
                                "<dark_red>Click to open an Mystical Cube!</dark_red>".toMiniMessage()
                                    .wrapped()
                            )
                    )
                )
                .build()

            val window = Window.single()
                .setViewer(sender)
                .setTitle("Spirit Cubes")
                .setGui(gui)
                .build()

            window.open()
        }
    }
}