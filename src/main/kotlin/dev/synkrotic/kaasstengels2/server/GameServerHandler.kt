package dev.synkrotic.kaasstengels2.server

import dev.synkrotic.kaasstengels2.server.classes.Player
import dev.synkrotic.kaasstengels2.server.data.GetHandler
import dev.synkrotic.kaasstengels2.server.data.PostHandler
import org.springframework.web.socket.*
import org.springframework.web.socket.handler.TextWebSocketHandler
import tools.jackson.databind.ObjectMapper

class GameServerHandler : TextWebSocketHandler() {
    override fun afterConnectionEstablished(session: WebSocketSession) {
        val name = getQueryParam(session, "name") ?: "Unknown"
        val pwd = getQueryParam(session, "pwd")

        if (pwd != null) { // Host attempt
            if (!GameServerConfig.isHost(name, pwd)) {
                session.close(CloseStatus.POLICY_VIOLATION) // Wrong password
                return
            }
            PostHandler.setAdmin(session)
            return
        }

        // Client attempt
        val player = Player(name, session)
        PostHandler.addNewUser(player)

        val message = ServerMessages.PLAYER_JOINED.param(
            "{player_name}",
            player.name
        ).toString()
        broadcast(
            message,
            player.session.id
        )
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        if (message.payload.toString().startsWith(ServerMessages.GET_PREFIX.toString())) {
            println("Get request")
            handleGetRequests(session, message.payload.toString())
        } else if (message.payload.toString().startsWith(ServerMessages.POST_PREFIX.toString())) {
            println("Post request")
        //            TODO: Add post requests
        }
    }
    fun handleGetRequests(session: WebSocketSession, message: String) {
        if (message.trim() == ServerMessages.GET_PLAYERS.toString()) {
            val playerList = GetHandler.getPlayerList().map { it.name }
            session.sendMessage(TextMessage(ObjectMapper().writeValueAsString(playerList)))
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val player = PostHandler.removeUser(session) ?: return // Admin logged out, no need to alert

        broadcast(
            ServerMessages.PLAYER_LEFT.param(
                "{player_name}",
                player.name)
        .toString())
    }

    private fun broadcast(message: String, excludeId: String? = null, excludeName: String? = null) {
        GetHandler.getAdmin()?.sendMessage(TextMessage(message))
        GetHandler.getPlayerList()
            .filter { it.name != excludeName
                        && it.session.id != excludeId
                        && it.session.isOpen }
            .forEach { it.session.sendMessage(TextMessage(message)) }
    }

    private fun getQueryParam(session: WebSocketSession, key: String): String? {
        return session.uri?.query
            ?.split("&")
            ?.find { it.startsWith("$key=") }
            ?.removePrefix("$key=")
    }
}