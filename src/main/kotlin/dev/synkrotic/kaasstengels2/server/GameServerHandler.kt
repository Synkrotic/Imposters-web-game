package dev.synkrotic.kaasstengels2.server

import dev.synkrotic.kaasstengels2.PlayerAnswer
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
            "player_name",
            player.name
        ).toString()
        broadcast(
            message,
            player.session.id
        )
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        println(message.payload.toString())
        if (message.payload.toString().startsWith(ServerMessages.GET_PREFIX.param("request", "").toString())) {
            println("Get request")
            handleGetRequests(session, message.payload.toString())
        } else if (message.payload.toString().startsWith(ServerMessages.POST_PREFIX.param("request", "").toString())) {
            println("Post request")
            handlePostRequests(session, message.payload.toString())
        }
    }
    fun handleGetRequests(session: WebSocketSession, message: String) {
        when (message.trim()) {
            ServerMessages.GET_PLAYERS.toString() -> {
                val playerList = GetHandler.getPlayerList().map { it.name }
                session.sendMessage(TextMessage(ObjectMapper().writeValueAsString(playerList)))
            }
            ServerMessages.GET_QUESTION.toString() -> {
                val player = GetHandler.getPlayerBySession(session) ?: return
                val question: String = GetHandler.getCurrentGame()?.currentRound?.getPlayerQuestion(player) ?: "Unknown"
                session.sendMessage(TextMessage(question))
            }

            ServerMessages.GET_ANSWERS.toString() -> {
                val answerList = GetHandler.getCurrentGame()?.currentRound?.answers
                session.sendMessage(TextMessage(ObjectMapper().writeValueAsString(answerList)))
            }
        }
    }
    fun handlePostRequests(session: WebSocketSession, message: String) {
        if (message.trim() == ServerMessages.START_GAME.toString()) {
            PostHandler.startGame(this)
        } else if (message.trim().startsWith(ServerMessages.GIVE_ANSWER.toString())) {
            val answer = message.trim().removePrefix(ServerMessages.GIVE_ANSWER.toString()).removeSurrounding(ServerMessages.GIVE_ANSWER.toString()).trim()
            val player = GetHandler.getPlayerBySession(session) ?: return
            val playerAnswer = PlayerAnswer(player, answer)
            PostHandler.registerAnswer(playerAnswer)
            session.sendMessage(TextMessage(ServerMessages.LOAD_SCREEN.param("screen_name", "wait").toString()))
        } else if (message.trim().startsWith(ServerMessages.GIVE_GUESS.toString())) {
            val guess = message.trim().removePrefix(ServerMessages.GIVE_GUESS.toString()).removeSurrounding(ServerMessages.GIVE_GUESS.toString()).trim()
            val player = GetHandler.getPlayerBySession(session) ?: return
            
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val player = PostHandler.removeUser(session) ?: return // Admin logged out, no need to alert

        broadcast(
            ServerMessages.PLAYER_LEFT.param(
                "player_name",
                player.name)
        .toString())
    }

    fun broadcast(message: String, excludeId: String? = null, excludeName: String? = null) {
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