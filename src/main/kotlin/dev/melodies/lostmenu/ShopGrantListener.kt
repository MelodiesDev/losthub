package dev.melodies.lostmenu

import dev.melodies.utils.toMiniMessage
import dev.melodies.utils.wrapped
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import xyz.xenondevs.invui.item.builder.ItemBuilder

class ShopGrantListener : Listener {
    companion object {
        val KEY = NamespacedKey("lost-items", "emerald")

        val EMERALD: ItemStack = ItemBuilder(Material.EMERALD)
            .setDisplayName("<gradient:aqua:dark_purple>Shop</gradient>".toMiniMessage().wrapped())
            .addLoreLines("<dark_purple>Open the Shop.</dark_purple>".toMiniMessage().wrapped())
            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
            .get()
            .also { item ->
                item.editMeta {
                    it.persistentDataContainer.set(KEY, PersistentDataType.BOOLEAN, true)
                }
            }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val inv = player.inventory

        inv.setItem(7, (EMERALD))
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.currentItem?.isSimilar(EMERALD) == true
        ) {
            event.isCancelled = true
        }
    }
}