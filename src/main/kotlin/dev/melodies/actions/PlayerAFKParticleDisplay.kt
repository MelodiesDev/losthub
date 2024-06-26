package dev.melodies.actions

import dev.melodies.utils.toMiniMessage
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Particle.DustOptions
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class PlayerAFKParticleDisplay(private val plugin: JavaPlugin) : Listener {

    private val lastSeen = mutableMapOf<UUID, Long>()

    private var afkPlayers = mutableSetOf<UUID>()

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        lastSeen[event.player.uniqueId] = System.currentTimeMillis()
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (event.hasChangedPosition()) {
            this.lastSeen[event.player.uniqueId] = System.currentTimeMillis()
            afkPlayers.remove(event.player.uniqueId)
        }
    }

    private fun update() {
        for (player in Bukkit.getOnlinePlayers()) {
            val lastSeenTime = lastSeen[player.uniqueId] ?: continue
            val now = System.currentTimeMillis()
            if (now - lastSeenTime > 1000 * 120) {
                if (afkPlayers.add(player.uniqueId)) {
                    player.sendMessage("<red>You are now AFK!</red>".toMiniMessage())
                }

                val r = 1.0 // radius
                val numOfParticles = 100 // number of particles you want to spawn

                val index = Bukkit.getCurrentTick() % numOfParticles
                val theta = 2.0 * Math.PI * index / numOfParticles
                val z = r * cos(theta)
                val x = r * sin(theta)

                val location = player.location.clone().add(z, 1.0, x)
                player.world.spawnParticle(
                    org.bukkit.Particle.DUST,
                    location,
                    0,
                    0.1,
                    1.0,
                    1.0,
                    0.0,
                    DustOptions(Color.RED, 1.0f)
                )
            }
        }
    }

    fun init() {
        Bukkit.getPluginManager().registerEvents(this, plugin)
        Bukkit.getScheduler().runTaskTimer(plugin, this::update, 0, 1)
    }
}