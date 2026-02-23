package dev.synkrotic.kaasstengels2.server

class ServerMessages {
    companion object {
        const val SERVER_PREFIX = "[SERVER]"

        val LOAD_SCREEN = ServerMessages("$SERVER_PREFIX LOAD SCREEN {screen_name}")

        val PLAYER_PREFIX = ServerMessages("$SERVER_PREFIX PLAYER")
        val PLAYER_JOINED = ServerMessages("$PLAYER_PREFIX JOINED {player_name}")
        val PLAYER_LEFT = ServerMessages("$PLAYER_PREFIX LEFT {player_name}")

        val GET_PREFIX = ServerMessages("$SERVER_PREFIX GET {request}")
        val GET_PLAYERS = GET_PREFIX.param("request", "PLAYER-LIST")
        val GET_QUESTION = GET_PREFIX.param("request", "QUESTION")
        val GET_ANSWERS = GET_PREFIX.param("request", "ANSWERS")

        val POST_PREFIX = ServerMessages("$SERVER_PREFIX POST {request}")
        val POST_GAME_PREFIX = POST_PREFIX.param("request", "GAME {action}")
        val START_GAME = POST_GAME_PREFIX.param("action", "START")
        val GIVE_ANSWER = POST_GAME_PREFIX.param("action", "ANSWER")
        val GIVE_GUESS = POST_GAME_PREFIX.param("action", "GUESS")
    }

    private var message: String

    constructor(message: String) {
        this.message = message
    }

    fun param(paramKey: String, paramValue: String): ServerMessages {
        return ServerMessages(message.replace("{$paramKey}", paramValue))
    }

    override fun toString(): String {
        return message
    }
}