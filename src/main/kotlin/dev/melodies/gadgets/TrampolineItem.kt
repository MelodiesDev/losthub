package dev.melodies.gadgets

import dev.melodies.losthub.LostHubPlugins
import dev.melodies.utils.toMiniMessage
import dev.melodies.utils.wrapped
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import xyz.xenondevs.invui.item.builder.ItemBuilder

class TrampolineItem(private val plugin: LostHubPlugins) : Listener {
    companion object {
        val KEY = NamespacedKey("gadgets", "trampoline")

        val TRAMPOLINE: ItemStack = ItemBuilder(Material.SLIME_BALL)
            .setDisplayName("<gradient:green:aqua>Trampoline</gradient>".toMiniMessage().wrapped())
            .addLoreLines("<dark_purple>Click to summon a trampoline!</dark_purple>".toMiniMessage().wrapped())
            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
            .get()
            .also { item ->
                item.editMeta {
                    it.persistentDataContainer.set(KEY, PersistentDataType.BOOLEAN, true)
                }
            }
    }

    fun setTemporaryTrampolineBlock(block: Block, delay: Long) {
        // Save the original block state
        val originalBlockState = block.blockData.clone()

        // Change the block to a slime block
        block.type = Material.SLIME_BLOCK

        // Set a delay to revert the block back to its original state
        object : BukkitRunnable() {
            override fun run() {
                // Restore the original block state
                block.blockData = originalBlockState
            }
        }.runTaskLater(plugin, delay)
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val block = player.location.subtract(0.0, 1.0, 0.0).block

        if (block.type == Material.SLIME_BLOCK) {
            // You can adjust the y parameter to control how high the player bounces.
            // The numbers 0.0 and 0.0 are the x and z (horizontal) velocities,
            // which we don't change in this specific case.
            player.velocity = Vector(0.0, 1.5, 0.0)
        }
    }

    @EventHandler
    fun summonTrampoline(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return

        val player = event.player
        val gadget = event.item ?: return
        if (gadget.itemMeta?.persistentDataContainer?.has(KEY) == false) return

        if (player.inventory.itemInMainHand.isSimilar(TRAMPOLINE)) {
            setTemporaryTrampolineBlock(event.clickedBlock!!, 90L)
        }
    }
}