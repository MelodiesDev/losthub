package dev.melodies.losthub

import dev.melodies.commands.OpenInfoBookCommand
import dev.melodies.commands.ServerCommands
import dev.melodies.losthubfeats.PlayerDoubleJump
import dev.melodies.lostmenu.CompassGrantListener
import dev.melodies.lostmenu.OpenNavigatorListener
import dev.melodies.lostmenu.OpenShopListener
import dev.melodies.lostmenu.ShopGrantListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class LostHubPlugins : JavaPlugin(){
    override fun onEnable() {
        logger.info("LostHub has been enabled!")
        Bukkit.getPluginManager().registerEvents(CompassGrantListener(), this)
        Bukkit.getPluginManager().registerEvents(OpenNavigatorListener(this), this)
        Bukkit.getPluginManager().registerEvents(ShopGrantListener(), this)
        Bukkit.getPluginManager().registerEvents(OpenShopListener(), this)
        Bukkit.getPluginManager().registerEvents(PlayerDoubleJump(this), this)
        this.getCommand("info")?.setExecutor(OpenInfoBookCommand())
        this.getCommand("hub")?.setExecutor(ServerCommands(this))

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord")
    }
}