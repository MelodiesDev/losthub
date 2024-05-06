package dev.melodies.lostmenu

import dev.melodies.utils.toMiniMessage
import dev.melodies.utils.wrapped
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem
import xyz.xenondevs.invui.window.Window

class OpenShopListener : Listener {
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return

        val item = event.item ?: return
        if (item.itemMeta?.persistentDataContainer?.has(ShopGrantListener.KEY) == false) return

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
                            "<gradient:dark_purple:light_purple>Click to open the shop in your browser</gradient>".toMiniMessage().wrapped()
                        )
                )
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
