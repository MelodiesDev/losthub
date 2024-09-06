package dev.melodies.utils.player

object PlayerDataStorage {
    private val data = mutableMapOf<String, PlayerStats>()

    fun getPlayerStats(playerId: String): PlayerStats? {
        return data[playerId]
    }

    fun setPlayerStats(playerId: String, xp: Int, coins: Int) {
        data[playerId] = PlayerStats(xp, coins)
    }
}

data class PlayerStats(val xp: Int, val coins: Int)