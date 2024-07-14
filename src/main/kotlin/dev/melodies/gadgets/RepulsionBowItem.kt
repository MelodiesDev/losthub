package dev.melodies.gadgets

import dev.melodies.losthub.LostHubPlugins
import dev.melodies.utils.toMiniMessage
import dev.melodies.utils.wrapped
import org.bukkit.Material
import org.bukkit.NamespacedKey
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

class RepulsionBowItem : Listener {
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
            // Get the shot arrow.
            player.sendMessage("<red>You have shot a Repulsion Bow!</red>".toMiniMessage())
            if (event.entity !is Arrow) return
            // TODO: add cool particles
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