package io.github.yuuuu51.othello

import kotlin.system.exitProcess

class Game {

    companion object {
        const val STATE_WAITING = 0
        const val STATE_PLAYING = 1
        const val STATE_FINISH = 2

        const val PLAYER_COLOR = Disc.STATE_BLACK
        const val CPU_COLOR = Disc.STATE_WHITE
    }

    private var status = STATE_WAITING

    val field = Field()

    private val cpu = CPU(this)

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

    fun start() {
        if (status != STATE_WAITING) {
            return
        }
        field.getDisc(4, 4)?.status = Disc.STATE_WHITE
        field.getDisc(5, 4)?.status = Disc.STATE_BLACK
        field.getDisc(4, 5)?.status = Disc.STATE_BLACK
        field.getDisc(5, 5)?.status = Disc.STATE_WHITE
        updateView()
        status = STATE_PLAYING

        while (true) {
            var playerDisc: Disc
            while (true) {
                playerDisc = getDiscByInput()
                if (!canSet(playerDisc, PLAYER_COLOR)) {
                    println("- You cannot set disc here")
                } else {
                    break
                }
            }
            setDisc(playerDisc, PLAYER_COLOR)
            updateView()
            judge()
            println("Player set a disc(${playerDisc.x}, ${playerDisc.y})")
            val cpuDisc = cpu.getNextDisc()
            setDisc(cpuDisc, CPU_COLOR)
            updateView()
            println("CPU set a disc(${cpuDisc.x}, ${cpuDisc.y})")
            judge()
        }
    }

    private fun judge() {
        var black = 0
        var white = 0
        for (x in 1..8) {
            for (y in 1..8) {
                when (field.getDisc(x, y)?.status) {
                    Disc.STATE_WHITE -> {
                        white++
                    }
                    Disc.STATE_BLACK -> {
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

    private fun waitInput(message: String): String {
        print("$message: ")
        return readLine()!!
    }

    private fun getDiscByInput(): Disc {
        val x: Int
        while (true) {
            val i = waitInput("Please enter x coordinate").toIntOrNull()
            if (i is Int && i in 1..8) {
                x = i
                break
            }
        }
        val y: Int
        while (true) {
            val i = waitInput("Please enter y coordinate").toIntOrNull()
            if (i is Int && i in 1..8) {
                y = i
                break
            }
        }
        return field.getDisc(x, y)!!
    }

    private fun canSet(disc: Disc, color: Int): Boolean {
        if (color != PLAYER_COLOR && color != CPU_COLOR) {
            return false
        }
        if (disc.status != Disc.STATE_EMPTY) {
            return false
        }
        around.forEach {
            var x = disc.x + it.first
            var y = disc.y + it.second
            var distance = 0
            while (true) {
                val disc2 = field.getDisc(x, y)
                if (disc2 !is Disc || disc2.status == Disc.STATE_EMPTY) {
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

    private fun setDisc(disc: Disc, color: Int) {
        require(canSet(disc, color))
        require(color == PLAYER_COLOR || color == CPU_COLOR)
        disc.status = color
        around.forEach {
            var x = disc.x + it.first
            var y = disc.y + it.second
            val cache = mutableListOf<Disc>()
            while (true) {
                val disc2 = field.getDisc(x, y)
                if (disc2 !is Disc || disc2.status == Disc.STATE_EMPTY) {
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
                    Disc.STATE_BLACK -> {
                        "○"
                    }
                    Disc.STATE_WHITE -> {
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