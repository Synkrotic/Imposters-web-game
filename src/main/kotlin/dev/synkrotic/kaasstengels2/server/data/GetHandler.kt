package dev.synkrotic.kaasstengels2.server.data

import dev.synkrotic.kaasstengels2.server.classes.Player
import org.springframework.web.socket.WebSocketSession

class GetHandler {
    companion object {
        fun getPlayerList(): List<Player> {
            return VolatileGameData.players
        }
        fun getAdmin(): WebSocketSession? = VolatileGameData.admin
    }
}