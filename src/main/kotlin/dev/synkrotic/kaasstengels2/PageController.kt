package dev.synkrotic.kaasstengels2

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class PageController {
    @GetMapping("/")
    fun index(model: Model): String = "forward:index.html"

    @GetMapping("/host")
    fun host(model: Model): String = "forward:host.html"

    @GetMapping("/client")
    fun client(model: Model): String = "forward:client.html"

    @GetMapping("/error")
    fun error(model: Model): String = "forward:client.html"
}