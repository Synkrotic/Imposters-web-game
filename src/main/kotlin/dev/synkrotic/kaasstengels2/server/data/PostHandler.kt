package dev.synkrotic.kaasstengels2.server.data

import dev.synkrotic.kaasstengels2.server.classes.Player
import org.springframework.web.socket.WebSocketSession

class PostHandler {
    companion object {
        fun setAdmin(session: WebSocketSession) {
            VolatileGameData.admin = session
        }

        fun addNewUser(player: Player): Player {
            VolatileGameData.players.add(player)
            return player
        }

        fun removeUser(session: WebSocketSession): Player? {
            if (VolatileGameData.admin == session) {
                VolatileGameData.admin = null
                return null
            }

            val player = VolatileGameData.players.first { it.session == session }
            VolatileGameData.players.removeIf { it.session == session }
            return player
        }
    }
}