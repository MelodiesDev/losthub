package dev.melodies.actions

import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Particle.DustOptions
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
import org.joml.Matrix4d
import org.joml.Vector3d
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

                // Play a cool ass funking sound.jpegS
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
                    org.bukkit.Particle.BLOCK,
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
        val player = event.player

        if (player.location.block.getRelative(BlockFace.DOWN, 5).isSolid) return
        if (!event.isFlying) return

        // Cancel the flight
        event.isCancelled = true

        // Allow the player to glide
        event.player.isGliding = true
        player.velocity = player.eyeLocation.direction.multiply(1)

        val r1 = 1.0
        val r2 = 5.0
        val r3 = 10.0// radius - how "big" the circle is
        val numOfParticles1 = 100
        val numOfParticles2 = 200
        val numOfParticles3 = 300// number of particles you want to spawn

        // Center point
        val center = Vector3d(player.location.x, player.location.y, player.location.z)

        for (i in 0 until numOfParticles1) {
            val theta = 2.0 * Math.PI * i / numOfParticles1
            val o1 = r1 * cos(theta)
            val o2 = r1 * sin(theta)

            // At this point, the circle is on the ground, laying flat
            val point = Vector3d(center.x + o1, center.y, center.z + o2)
            val transformed = Matrix4d().translate(center)
                // Rotate the circle to be upright
                .rotate(Math.toRadians(90.0), 1.0, 0.0, 0.0)
                // Rotate the circle to be facing the player
                .rotate(Math.toRadians(player.location.pitch.toDouble()), -1.0, 0.0, 0.0)
                .rotate(Math.toRadians(player.location.yaw.toDouble()), 0.0, 0.0, 1.0)
                .translate(center.negate())
                .transformPosition(point)

            player.world.spawnParticle(
                org.bukkit.Particle.DUST,
                transformed.x,
                transformed.y,
                transformed.z,
                1,
                0.0,
                0.0,
                0.0,
                DustOptions(Color.WHITE, 1f)
            )
        }
        for (i in 0 until numOfParticles2) {
            val theta = 2.0 * Math.PI * i / numOfParticles2
            val o1 = r2 * cos(theta)
            val o2 = r2 * sin(theta)

            // At this point, the circle is on the ground, laying flat
            val point = Vector3d(center.x + o1, center.y, center.z + o2)
                .sub(player.location.direction.multiply(3).toVector3d())

            val transformed = Matrix4d().translate(center)
                // Rotate the circle to be upright
                .rotate(Math.toRadians(90.0), 1.0, 0.0, 0.0)
                // Rotate the circle to be facing the player
                .rotate(Math.toRadians(player.location.pitch.toDouble()), -1.0, 0.0, 0.0)
                .rotate(Math.toRadians(player.location.yaw.toDouble()), 0.0, 0.0, 1.0)
                .translate(center.negate())
                .transformPosition(point)

            player.world.spawnParticle(
                org.bukkit.Particle.DUST,
                transformed.x,
                transformed.y,
                transformed.z,
                3,
                0.0,
                0.0,
                0.0,
                DustOptions(Color.WHITE, 2f)
            )
        }
        for (i in 0 until numOfParticles3) {
            val theta = 2.0 * Math.PI * i / numOfParticles3
            val o1 = r3 * cos(theta)
            val o2 = r3 * sin(theta)

            // At this point, the circle is on the ground, laying flat
            val point = Vector3d(center.x + o1, center.y, center.z + o2)
                .sub(player.location.direction.multiply(5).toVector3d())

            val transformed = Matrix4d().translate(center)
                // Rotate the circle to be upright
                .rotate(Math.toRadians(90.0), 1.0, 0.0, 0.0)
                // Rotate the circle to be facing the player
                .rotate(Math.toRadians(player.location.pitch.toDouble()), -1.0, 0.0, 0.0)
                .rotate(Math.toRadians(player.location.yaw.toDouble()), 0.0, 0.0, 1.0)
                .translate(center.negate())
                .transformPosition(point)

            player.world.spawnParticle(
                org.bukkit.Particle.DUST,
                transformed.x,
                transformed.y,
                transformed.z,
                5,
                0.0,
                0.0,
                0.0,
                DustOptions(Color.WHITE, 3f)
            )
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