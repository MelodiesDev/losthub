package dev.melodies.utils.commands

import java.util.UUID

object CommandCooldownStorage {
    private val cooldownMap = HashMap<UUID, Long>()

    fun getCooldown(playerUUID: UUID): Long? {
        return cooldownMap[playerUUID]
    }

    fun setCooldown(playerUUID: UUID, timestamp: Long) {
        cooldownMap[playerUUID] = timestamp
    }
}