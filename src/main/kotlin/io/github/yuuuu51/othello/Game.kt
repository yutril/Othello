package io.github.yuuuu51.othello

import io.github.yuuuu51.othello.player.CPUPlayer
import io.github.yuuuu51.othello.player.Player
import io.github.yuuuu51.othello.player.UserPlayer
import kotlin.system.exitProcess

class Game {

    companion object {
        const val STATE_WAITING = 0
        const val STATE_PLAYING = 1
        const val STATE_FINISH = 2

        const val COLOR_EMPTY = 0
        const val COLOR_BLACK = 1
        const val COLOR_WHITE = 2
    }

    val field = Field()

    private var status = STATE_WAITING

    private val blackPlayer: Player

    private val whitePlayer: Player

    private val around = listOf(
        -1 to 1,
        0 to 1,
        1 to 1,
        -1 to 0,
        1 to 0,
        -1 to -1,
        0 to -1,
        1 to -1
    )

    init {
        blackPlayer = UserPlayer(this, COLOR_BLACK)
        whitePlayer = CPUPlayer(this, COLOR_WHITE)
    }

    fun start() {
        if (status != STATE_WAITING) {
            return
        }
        field.getDisc(4, 4)?.status = COLOR_WHITE
        field.getDisc(5, 4)?.status = COLOR_BLACK
        field.getDisc(4, 5)?.status = COLOR_BLACK
        field.getDisc(5, 5)?.status = COLOR_WHITE
        updateView()
        status = STATE_PLAYING

        var player: Player = whitePlayer
        while (true) {
            player = when (player) {
                blackPlayer -> whitePlayer
                whitePlayer -> blackPlayer
                else -> throw Exception()
            }
            if (getCanSetDiscs(player.color).isEmpty()) {
                println("${player.getName()} could not set any discs")
                continue
            }
            val disc = player.getNextDisc()
            setDisc(disc, player.color)
            updateView()
            println("${player.getName()} set a disc(${disc.x}, ${disc.y})")
            judge()
        }
    }

    fun canSet(disc: Disc, color: Int): Boolean {
        if (color != COLOR_BLACK && color != COLOR_WHITE) {
            return false
        }
        if (disc.status != COLOR_EMPTY) {
            return false
        }
        around.forEach {
            var x = disc.x + it.first
            var y = disc.y + it.second
            var distance = 0
            while (true) {
                val disc2 = field.getDisc(x, y)
                if (disc2 !is Disc || disc2.status == COLOR_EMPTY) {
                    return@forEach
                }
                if (disc2.status != color) {
                    distance++
                    x += it.first
                    y += it.second
                    continue
                } else {
                    if (distance > 0) {
                        return true
                    }
                    return@forEach
                }
            }
        }
        return false
    }

    fun getCanSetDiscs(color: Int): List<Disc> {
        val discs = mutableListOf<Disc>()
        field.getAllDisc().forEach { (_, map) ->
            map.forEach { (_, disc) ->
                if (canSet(disc, color)) {
                    discs.add(disc)
                }
            }
        }
        return discs
    }

    private fun judge() {
        if (getCanSetDiscs(COLOR_BLACK).isNotEmpty() || getCanSetDiscs(COLOR_WHITE).isNotEmpty()) {
            return
        }
        var black = 0
        var white = 0
        for (x in 1..8) {
            for (y in 1..8) {
                when (field.getDisc(x, y)?.status) {
                    COLOR_WHITE -> {
                        white++
                    }
                    COLOR_BLACK -> {
                        black++
                    }
                    else -> {
                        return
                    }
                }
            }
        }
        val winner = when {
            black > white -> {
                "Black win!"
            }
            white > black -> {
                "White win!"
            }
            else -> {
                "Draw!"
            }
        }
        status = STATE_FINISH
        print("$winner (Black: $black, White: $white)")
        exitProcess(0)
    }

    private fun setDisc(disc: Disc, color: Int) {
        require(canSet(disc, color))
        require(color == COLOR_BLACK || color == COLOR_WHITE)
        disc.status = color
        around.forEach {
            var x = disc.x + it.first
            var y = disc.y + it.second
            val cache = mutableListOf<Disc>()
            while (true) {
                val disc2 = field.getDisc(x, y)
                if (disc2 !is Disc || disc2.status == COLOR_EMPTY) {
                    return@forEach
                }
                if (disc2.status == color) {
                    cache.forEach { disc3 ->
                        disc3.status = color
                    }
                    return@forEach
                }
                cache.add(disc2)
                x += it.first
                y += it.second
            }
        }
    }

    private fun updateView() {
        var s = ""
        /*
        var count = 0
        while(count < lastOutput.count()) {
            s += "\b"
            count++
        }
         */
        s += "\nPlayer: black(○)\n  1 2 3 4 5 6 7 8 x\n"
        var key = 0
        for (y in 1..8) {
            s += y
            for (x in 1..8) {
                val disc = field.getDisc(x, y)
                val view = when (disc?.status) {
                    COLOR_BLACK -> {
                        "○"
                    }
                    COLOR_WHITE -> {
                        "●"
                    }
                    else -> {
                        "-"
                    }
                }
                s += " $view"
                key++
            }
            s += "\n"
        }
        s += "y\n"
        print(s)
    }
}