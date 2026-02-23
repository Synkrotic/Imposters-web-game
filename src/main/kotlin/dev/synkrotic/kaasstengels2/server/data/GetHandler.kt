package dev.synkrotic.kaasstengels2.server.data

import dev.synkrotic.kaasstengels2.ImposterGame
import dev.synkrotic.kaasstengels2.server.classes.Player
import org.springframework.web.socket.WebSocketSession

class GetHandler {
    companion object {
        fun getPlayerList(): List<Player> = VolatileGameData.players
        fun getPlayerBySession(session: WebSocketSession): Player? = VolatileGameData.players.find { it.session == session }
        fun getAdmin(): WebSocketSession? = VolatileGameData.admin
        fun getCurrentGame(): ImposterGame? = VolatileGameData.currentGame
    }
}