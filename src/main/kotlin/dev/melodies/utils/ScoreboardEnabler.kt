package dev.melodies.utils

import io.papermc.paper.scoreboard.numbers.NumberFormat
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Score
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.ScoreboardManager

class ScoreboardEnabler : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player

        // Create a Scoreboard Manager
        val manager: ScoreboardManager = Bukkit.getScoreboardManager()

        // Create a new Scoreboard
        val board: Scoreboard = manager.newScoreboard

        // Create the Objective (displayed name)
        val objective = board.registerNewObjective("test", "dummy", "<gradient:aqua:dark_purple><b>LostOasis</b></gradient>".toMiniMessage())
        objective.displaySlot = DisplaySlot.SIDEBAR
        objective.numberFormat(NumberFormat.blank())

        val blank1: Score = objective.getScore("blank1")
        blank1.customName(Component.empty())
        blank1.score = 6

        val score: Score = objective.getScore(player.name)
        score.score = 5

        val blank2: Score = objective.getScore("blank2")
        blank2.customName(Component.empty())
        blank2.score = 4

        val coins: Score = objective.getScore("coins")
        coins.customName("<gold>Coins".toMiniMessage())
        coins.score = 3

        val blank3: Score = objective.getScore("blank3")
        blank3.customName(Component.empty())
        blank3.score = 2

        val exp: Score = objective.getScore("exp")
        exp.customName("<dark_purple>Experience".toMiniMessage())
        exp.score = 1

        val blank4: Score = objective.getScore("blank4")
        blank4.customName(Component.empty())
        blank4.score = 0

        // Assign the scoreboard to the player
        player.scoreboard = board
    }
}