package dev.melodies.actions

import dev.melodies.utils.player.PlayerVault
import dev.melodies.utils.player.VaultStorage
import dev.melodies.utils.toMiniMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.Inventory

class VaultOpener : Listener {

    companion object {
        fun openVault(player: Player) {
            val vaultInventory: Inventory =
                Bukkit.createInventory(player, InventoryType.CHEST, "Personal Vault".toMiniMessage())

            val playerVault = VaultStorage.getPlayerStorage(player)

            for ((slot, item) in playerVault.items) {
                vaultInventory.setItem(slot, item)
            }

            player.openInventory(vaultInventory)
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return

        if (event.inventory == "Personal Vault".toMiniMessage()) {
            if (event.clickedInventory?.type == InventoryType.PLAYER) {
                event.isCancelled = false
            } else {
                val vault = VaultStorage.getPlayerStorage(player).items.toMutableMap()
                vault[event.slot] = event.currentItem?.clone() ?: return
                VaultStorage.setPlayerStorage(player, PlayerVault(vault))
            }
        }
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val vault = VaultStorage.getPlayerStorage(event.player).items.toMutableMap()
        for (i in 0 until event.inventory.size) {
            val item = event.inventory.getItem(i)
            if (item != null) {
                vault[i] = item
            } else {
                vault.remove(i)
            }
            VaultStorage.setPlayerStorage(event.player, PlayerVault(vault))
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        VaultStorage.setPlayerStorage(player, PlayerVault(mapOf()))
    }
}
