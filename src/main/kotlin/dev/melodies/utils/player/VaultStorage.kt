package dev.melodies.utils.player

import org.bukkit.entity.HumanEntity
import org.bukkit.inventory.ItemStack
import java.util.UUID

object VaultStorage {
    private val data = mutableMapOf<UUID, PlayerVault>()

    fun getPlayerStorage(player: HumanEntity): PlayerVault =
        data[player.uniqueId] ?: PlayerVault(mapOf())

    fun setPlayerStorage(player: HumanEntity, vault: PlayerVault) {
        data[player.uniqueId] = vault
    }
}

data class PlayerVault(val items: Map<Int, ItemStack>)