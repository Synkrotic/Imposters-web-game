package dev.synkrotic.kaasstengels2.server.data

import dev.synkrotic.kaasstengels2.ImposterGame
import dev.synkrotic.kaasstengels2.server.classes.Player
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.CopyOnWriteArrayList

class VolatileGameData {
    companion object {
        val players: CopyOnWriteArrayList<Player> = CopyOnWriteArrayList()
        var admin: WebSocketSession? = null
        var currentGame: ImposterGame? = null
    }
}