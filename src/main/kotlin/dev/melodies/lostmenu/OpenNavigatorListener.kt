package dev.melodies.lostmenu

import dev.melodies.utils.PlayerServerUtils
import dev.melodies.utils.toMiniMessage
import dev.melodies.utils.wrapped
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.Plugin
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem
import xyz.xenondevs.invui.window.Window

class OpenNavigatorListener(private val plugin: Plugin) : Listener {
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return

        val item = event.item ?: return
        if (item.itemMeta?.persistentDataContainer?.has(CompassGrantListener.KEY) == false) return

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
                    it.player.world.playSound(it.player.location, "minecraft:entity.armadillo.scute_drop", 0.5f, 1.0f)
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
}
