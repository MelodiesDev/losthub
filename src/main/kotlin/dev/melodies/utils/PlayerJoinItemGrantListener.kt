package dev.melodies.utils

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import xyz.xenondevs.invui.item.builder.ItemBuilder

class PlayerJoinItemGrantListener : Listener {
    companion object {
        val COMPASSKEY = NamespacedKey("lost-items", "compass")

        val COMPASS: ItemStack = ItemBuilder(Material.COMPASS)
            .setDisplayName("<gradient:aqua:dark_purple>Navigator</gradient>".toMiniMessage().wrapped())
            .addLoreLines("<dark_purple>A dimensional Navigator.</dark_purple>".toMiniMessage().wrapped())
            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
            .get()
            .also { item ->
                item.editMeta {
                    it.persistentDataContainer.set(COMPASSKEY, PersistentDataType.BOOLEAN, true)
                }
            }

        val SHOPKEY = NamespacedKey("lost-items", "emerald")

        val EMERALD: ItemStack = ItemBuilder(Material.EMERALD)
            .setDisplayName("<gradient:aqua:dark_purple>Shop</gradient>".toMiniMessage().wrapped())
            .addLoreLines("<dark_purple>Open the Shop.</dark_purple>".toMiniMessage().wrapped())
            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
            .get()
            .also { item ->
                item.editMeta {
                    it.persistentDataContainer.set(SHOPKEY, PersistentDataType.BOOLEAN, true)
                }
            }

        val GADGETKEY = NamespacedKey("lost-items", "gadget")

        val GADGET: ItemStack = ItemBuilder(Material.REDSTONE)
            .setDisplayName("<gradient:aqua:dark_purple>Gadgets</gradient>".toMiniMessage().wrapped())
            .addLoreLines("<dark_purple>Open the Gadget Menu.</dark_purple>".toMiniMessage().wrapped())
            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
            .get()
            .also { item ->
                item.editMeta {
                    it.persistentDataContainer.set(GADGETKEY, PersistentDataType.BOOLEAN, true)
                }
            }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val inv = player.inventory

        var item = inv.getItem(8)
        if (item == null || item.isEmpty) {
            inv.setItem(8, (COMPASS))
        }
        item = inv.getItem(7)
        if (item == null || item.isEmpty) {
            inv.setItem(7, (EMERALD))
        }
        item = inv.getItem(6)
        if (item == null || item.isEmpty) {
            inv.setItem(6, (GADGET))
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.currentItem?.isSimilar(COMPASS) == true || event.currentItem?.isSimilar(EMERALD) == true || event.currentItem?.isSimilar(
                GADGET
            ) == true
        ) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onDrop(event: PlayerDropItemEvent) {
        if (event.itemDrop.itemStack.isSimilar(COMPASS) || event.itemDrop.itemStack.isSimilar(EMERALD) || event.itemDrop.itemStack.isSimilar(
                GADGET
            )
        ) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        event.keepInventory = true
    }
}