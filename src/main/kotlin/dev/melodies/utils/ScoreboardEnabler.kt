package dev.melodies.utils

import dev.melodies.utils.player.PlayerDataStorage
import io.papermc.paper.scoreboard.numbers.NumberFormat
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Score
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.ScoreboardManager

class ScoreboardEnabler(
    private val plugin: JavaPlugin
) : Listener {

    private fun updateScoreboard(player: Player) {
        val stats = PlayerDataStorage.getPlayerStats(player.uniqueId.toString())

        val currentCoins = stats?.coins ?: 0
        val currentExp = stats?.xp ?: 0

        // Create a Scoreboard Manager
        val manager: ScoreboardManager = Bukkit.getScoreboardManager()

        // Create a new Scoreboard
        val board: Scoreboard = manager.mainScoreboard

        // Create the Objective (displayed name)
        val objective = board.getObjective("test")
            ?: board.registerNewObjective("test", "dummy", "<gradient:aqua:dark_purple><b>LostOasis</b></gradient>".toMiniMessage())

        objective.displaySlot = DisplaySlot.SIDEBAR
        objective.numberFormat(NumberFormat.blank())

        val blank1: Score = objective.getScore("blank1")
        blank1.customName(Component.empty())
        blank1.score = 7

        val name: Score = objective.getScore(player.name)
        name.customName(("<blue>|</blue> <gradient:aqua:light_purple:white:light_purple:aqua>${player.name}</gradient>").toMiniMessage())
        name.score = 6

        val blank2: Score = objective.getScore("blank2")
        blank2.customName(Component.empty())
        blank2.score = 5

        val coins: Score = objective.getScore("coins")
        coins.customName(("<blue>|</blue> Coins: <gold>\$$currentCoins</gold>").toMiniMessage())
        coins.score = 4

        val blank3: Score = objective.getScore("blank3")
        blank3.customName(Component.empty())
        blank3.score = 3

        val exp: Score = objective.getScore("exp")
        exp.customName("<blue>|</blue> XP: <green>$currentExp%</green>".toMiniMessage())
        exp.score = 2

        val blank4: Score = objective.getScore("blank4")
        blank4.customName(Component.empty())
        blank4.score = 1

        val end: Score = objective.getScore("end")
        end.customName("<dark_gray> << lostoasis.com >></dark_gray>".toMiniMessage())
        end.score = 0

        // Assign the scoreboard to the player
        player.scoreboard = board
    }

    fun scheduleScoreboardUpdates() {
        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            for (player in Bukkit.getOnlinePlayers()) {
                updateScoreboard(player)
            }
        }, 0L, 20L * 5) // Run task every 5 seconds (100 ticks)
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        updateScoreboard(player)
    }
}