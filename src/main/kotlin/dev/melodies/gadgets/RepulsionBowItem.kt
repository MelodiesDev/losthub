package dev.melodies.gadgets

import dev.melodies.losthub.LostHubPlugins
import dev.melodies.utils.toMiniMessage
import dev.melodies.utils.wrapped
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.bukkit.*
import org.bukkit.Particle.DustOptions
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Arrow
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import xyz.xenondevs.invui.item.builder.ItemBuilder
import kotlin.math.cos
import kotlin.math.sin

class RepulsionBowItem(private val plugin: LostHubPlugins) : Listener {
    companion object {
        val KEY = NamespacedKey("gadgets", "repulsion")

        val REPULSION: ItemStack = ItemBuilder(Material.BOW)
            .setDisplayName("<gradient:green:aqua>Repulsion Bow</gradient>".toMiniMessage().wrapped())
            .addLoreLines("<dark_purple>Click to equip the bow!</dark_purple>".toMiniMessage().wrapped())
            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
            .setUnbreakable(true)
            .addEnchantment(Enchantment.INFINITY, 1, false)
            .get()
            .also { item ->
                item.editMeta {
                    it.persistentDataContainer.set(KEY, PersistentDataType.BOOLEAN, true)
                }
            }
    }

        @EventHandler
        fun onProjectileShoot(event: ProjectileLaunchEvent) {
            val player = event.entity.shooter as? Player ?: return
            if (event.entityType != EntityType.ARROW) return // If it isnt an arrow we don't care
            if (!player.inventory.itemInMainHand.isSimilar(REPULSION)) return // If it isn't the repulsion bow we don't care
            if (event.entity !is Arrow) return
            // TODO: add cool particles and make it shoot straight
            val arrow = event.entity as Arrow
            val look = player.eyeLocation.direction

            arrow.setGravity(false)
            arrow.velocity = look.multiply(0.5)
            arrow.world.playSound(arrow.location, "minecraft:entity.ender_dragon.ambient", 1.0f, 0.5f)
            arrow.world.playSound(arrow.location, "minecraft:entity.wolf.whine", 1.0f, 0.5f)

            Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
                val r = 1.0 // radius
                val numOfParticles = 100 // number of particles you want to spawn

                val index = Bukkit.getCurrentTick() % numOfParticles
                val theta = 5.0 * Math.PI * index / numOfParticles
                val z = r * cos(theta)
                val y = r * sin(theta)

                val location = arrow.location.clone().add(z, 0.0, y)
                arrow.world.spawnParticle(
                    org.bukkit.Particle.DUST,
                    location,
                    1,
                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    DustOptions(Color.BLUE, 1.5f)
                )

                val index2 = (Bukkit.getCurrentTick() + (numOfParticles / 2)) % numOfParticles
                val theta2 = 5.0 * Math.PI * index2 / numOfParticles
                val z2 = r * cos(theta2)
                val y2 = r * sin(theta2)

                val location2 = arrow.location.clone().add(z2, 0.0, y2)
                arrow.world.spawnParticle(
                    org.bukkit.Particle.DUST,
                    location2,
                    1,
                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    DustOptions(Color.BLUE, 1.5f)
                )
            }, 0L, 1L)
        }

        @EventHandler
        fun onProjectileHit(event: ProjectileHitEvent) {
            // Knockback players hit by the arrow
            val playerHit = event.hitEntity
            if (playerHit !is Player) return

            playerHit.velocity = event.entity.location.direction.multiply(5)
            playerHit.sendMessage("<red>You have been knocked back by the Repulsion Bow!</red>".toMiniMessage())
        }
}