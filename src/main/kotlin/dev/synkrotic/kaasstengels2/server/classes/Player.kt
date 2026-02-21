package dev.synkrotic.kaasstengels2.server.classes

import org.springframework.web.socket.WebSocketSession

data class Player(
    val name: String,
    val session: WebSocketSession
)
