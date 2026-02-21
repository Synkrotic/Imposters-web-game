package dev.synkrotic.kaasstengels2.server

import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.*

@Configuration
@EnableWebSocket
class GameServerConfig : WebSocketConfigurer {
    companion object {
        private const val HOST_NAME: String = "admin"
        private const val HOST_PWD: String = "admin"

        fun isHost(enteredName: String, enteredPwd: String) =
            (HOST_PWD == enteredPwd && HOST_NAME == enteredName)
    }


    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(GameServerHandler(), "/game-server").setAllowedOrigins("*")
    }
}