package dev.synkrotic.kaasstengels2.server.data

import dev.synkrotic.kaasstengels2.server.classes.Player
import org.springframework.web.socket.WebSocketSession

class VolatileGameData {
    companion object {
        val players: MutableList<Player> = mutableListOf()
        var admin: WebSocketSession? = null
    }
}