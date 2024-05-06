package dev.melodies.losthubfeats

import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.plugin.java.JavaPlugin
import kotlin.math.min

class PlayerDoubleJump(private val plugin: JavaPlugin) : Listener {

    @EventHandler
    fun onPlayerJump(event: PlayerToggleSneakEvent) {
        // We only care if they just started sneaking
        if (!event.isSneaking) return

        val player = event.player
        val blockBelow = player.location.block.getRelative(BlockFace.DOWN)
        if (!blockBelow.isSolid) return

        var chargeTime = 0.8f
        val chargeDelay = 10L

        Bukkit.getScheduler().runTaskTimer(plugin, { task ->
            // This runs every tick
            if (player.isSneaking) { // If the player is holding shift
                // Increment
                chargeTime++
                if (chargeTime <= chargeDelay) return@runTaskTimer

                // Play a cool ass funking sound.jpeg
                val pitch = min(chargeTime * 0.1f, 2f)
                player.playSound(player, Sound.BLOCK_SCULK_CHARGE, 1f, pitch)
            } else {
                // They stopped
                task.cancel()
                if (chargeTime <= chargeDelay) return@runTaskTimer

                val maxPower = 3.5
                val power = min((chargeTime - chargeDelay) * 0.2, maxPower)

                player.velocity = player.location.direction.multiply(min(power, maxPower))

                player.world.playSound(player, Sound.ITEM_TRIDENT_RIPTIDE_1, 0.5f, 2f)
                player.world.spawnParticle(
                    org.bukkit.Particle.BLOCK_CRACK,
                    player.location,
                    100,
                    1.0,
                    0.0,
                    1.0,
                    0.0,
                    blockBelow.blockData
                )

                startParticleTask(player)
            }
        }, 0L, 1L)
    }

//    @EventHandler
//    fun doubleTap(event: PlayerToggleFlightEvent) {
//        val player = event.player
//        player.allowFlight = true
//
//        if (player.location.block.getRelative(BlockFace.DOWN).isEmpty) {
//            event.player.isGliding = true
//            startParticleTask(player)
//        } else {
//            event.isCancelled = true
//        }
//    }

//    @EventHandler
//    fun canFly(event: EntityToggleGlideEvent) {
//        if (event.isGliding && event.entity.location.block.getRelative(BlockFace.DOWN).isEmpty) {
//            event.isCancelled = true
//        }
//    }


    private fun startParticleTask(player: Player) {
        Bukkit.getScheduler().runTaskTimer(plugin, { task2 ->
            if (player.location.block.getRelative(BlockFace.DOWN).isEmpty) {
                player.world.spawnParticle(org.bukkit.Particle.ELECTRIC_SPARK, player.location, 10, 0.25, 0.5, 0.25, 0.0)
                player.world.spawnParticle(org.bukkit.Particle.DRAGON_BREATH, player.location, 10, 0.0, 0.25, 0.0, 0.0)
            } else {
                task2.cancel()
            }
        }, 5L, 1L)
    }
}