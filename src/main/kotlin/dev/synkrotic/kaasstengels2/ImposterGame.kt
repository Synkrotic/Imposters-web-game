package dev.synkrotic.kaasstengels2

import dev.synkrotic.kaasstengels2.server.classes.Player
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.synkrotic.kaasstengels2.server.GameServerHandler
import dev.synkrotic.kaasstengels2.server.ServerMessages
import dev.synkrotic.kaasstengels2.server.data.GetHandler
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import jakarta.annotation.PostConstruct


class ImposterGame internal constructor(
    private val serverHandler: GameServerHandler,
    private val questionService: QuestionService,
) {
    lateinit var currentRound: Round

    fun start() {
        println("Starting Game")
        goPage("wait")

        Thread.sleep(3000)

        startNewRound()
    }

    fun onGameEnd() {

    }

    fun startNewRound() {
        this.currentRound = Round.newRandom(questionService)

        goPage("wait")
        Thread.sleep(1000)
        goPage("question")

        if (allAnswered(currentRound)) {
            Thread.sleep(3000)
            goPage("judge")
        }
    }

    fun allAnswered(round: Round): Boolean {
        if (round.allAnswered()) {
            return true
        }
        Thread.sleep(1000)
        return allAnswered(currentRound)
    }

    fun goPage(screenName: String) {
        serverHandler.broadcast(
            ServerMessages.LOAD_SCREEN
                .param("screen_name", screenName)
                .toString()
        )
    }

    fun goAdminPage(screenName: String) {
    }
}

class QuestionService {
    private lateinit var questions: List<QuestionPair>

    constructor() {
        loadQuestions()
    }

    fun loadQuestions() {
        val resource = ClassPathResource("questions.json")
        val jsonString = resource.inputStream
            .bufferedReader()
            .use { it.readText() }

        val listType = object : TypeToken<List<QuestionPair>>() {}.type
        questions = Gson().fromJson(jsonString, listType)
    }

    fun getAll(): List<QuestionPair> = questions

    fun getRandom(): QuestionPair =
        questions.random()
}

class Round internal constructor(
    private val imposter: Player,
    private val players: List<Player>,
    private val question: QuestionPair,
    val answers: MutableList<PlayerAnswer> = mutableListOf()
) {
    companion object {
        fun newRandom(questionService: QuestionService): Round {
            val players: MutableList<Player> = GetHandler.getPlayerList().shuffled().toMutableList()
            val imposter: Player = players.removeAt(0)

            return Round(
                imposter,
                players,
                questionService.getRandom()
            )
        }
    }

    fun getPlayerQuestion(player: Player): String {
        if (imposter == player) {
            return question.imposter
        }
        return question.regular
    }

    fun allAnswered(): Boolean {
        for (answer in answers) {
            if (answer.answer.isEmpty()) return false;
        }
        return answers.size == (players.size + 1) // +1 for imposter which is not in list
    }

    fun addAnswer(answer: PlayerAnswer) {
        answers.add(answer)
        println("NEW ANSWER! $answer")
    }
}

data class QuestionPair(
    val regular: String,
    val imposter: String
)

data class PlayerAnswer(
    val player: Player,
    val answer: String
)