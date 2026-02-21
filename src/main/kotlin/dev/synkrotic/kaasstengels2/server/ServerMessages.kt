package dev.synkrotic.kaasstengels2.server

class ServerMessages {
    companion object {
        const val SERVER_PREFIX = "[SERVER]"
        val PLAYER_JOINED = ServerMessages("$SERVER_PREFIX PLAYER JOINED {player_name}")
        val PLAYER_LEFT = ServerMessages("$SERVER_PREFIX PLAYER LEFT {player_name}")

        val GET_PREFIX = ServerMessages("$SERVER_PREFIX GET")
        val GET_PLAYERS = ServerMessages("$GET_PREFIX PLAYER-LIST")

        val POST_PREFIX = ServerMessages("$SERVER_PREFIX POST ")
    }

    private var message: String

    constructor(message: String) {
        this.message = message
    }

    fun param(paramKey: String, paramValue: String): ServerMessages {
        return ServerMessages(message.replace(paramKey, paramValue))
    }

    override fun toString(): String {
        return message
    }
}