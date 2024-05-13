package dev.melodies.losthubfeats

import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityToggleGlideEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.plugin.java.JavaPlugin
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

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

    @EventHandler
    fun onEntityToggleGlide(event: EntityToggleGlideEvent) {
        // Ignore if we're stopping the player from gliding.
        // If we don't filter this out, our attempt to start their
        // glide will also be canceled.
        if (event.isGliding) return

        // Prevent the player from stopping their glide
        // unless there is a block below them.
        val player = event.entity as? Player ?: return
        if (!player.location.block.getRelative(BlockFace.DOWN, 3).isEmpty) return




        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerToggleFlight(event: PlayerToggleFlightEvent) {
        // TODO: Fix this with creative mode
        // We only care if they just started flying
        val player = event.player

        if (!event.isFlying) return

        // Cancel the flight
        event.isCancelled = true

        // Allow the player to glide
        event.player.isGliding = true
        player.velocity = player.eyeLocation.direction.multiply(1)

        val r = 1.0 // radius
        val numOfParticles = 100 // number of particles you want to spawn

        for (i in 0 until numOfParticles) {
            val theta = 2.0 * Math.PI * i / numOfParticles
            val y = r * cos(theta)
            val z = r * sin(theta)

            val location = player.location.clone().add(0.0, y, z)
            player.world.spawnParticle(org.bukkit.Particle.GUST, location, 0, 0.1, 1.0, 1.0, 0.0)
        }
        player.world.playSound(player, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 0.5f, 0.1f)
        player.world.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR, 0.5f, 0.1f)
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.player.allowFlight = true
    }

    private fun startParticleTask(player: Player) {
        Bukkit.getScheduler().runTaskTimer(plugin, { task2 ->
            if (player.location.block.getRelative(BlockFace.DOWN).isEmpty) {
                player.world.spawnParticle(
                    org.bukkit.Particle.ELECTRIC_SPARK,
                    player.location,
                    10,
                    0.25,
                    0.5,
                    0.25,
                    0.0
                )
                player.world.spawnParticle(
                    org.bukkit.Particle.DRAGON_BREATH,
                    player.location,
                    10,
                    0.0,
                    0.25,
                    0.0,
                    0.0
                )
            } else {
                task2.cancel()
            }
        }, 5L, 1L)
    }
}