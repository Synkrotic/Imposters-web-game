package dev.synkrotic.kaasstengels2.server.classes

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.web.socket.WebSocketSession

data class Player(
    val name: String,
    @JsonIgnore
    val session: WebSocketSession
)
