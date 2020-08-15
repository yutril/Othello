package io.github.yuuuu51.othello

class Game {

    companion object {
        const val STATE_WAITING = 0
        const val STATE_PLAYING = 1
        const val STATE_FINISH = 2

        const val PLAYER_COLOR = Disc.STATE_BLACK
        const val CPU_COLOR = Disc.STATE_WHITE
    }

    var status = STATE_WAITING
        private set

    val field = Field()

    private val cpu = CPU(this)

    private val around = listOf(
        -1 to 1,
        0 to 1,
        1 to 1,
        -1 to 0,
        1 to 0,
        -1 to -1,
        -1 to 0,
        1 to 1
    )

    fun start() {
        if (status != STATE_WAITING) {
            return
        }
        field.getDisc(4, 4)?.status = Disc.STATE_WHITE
        field.getDisc(5, 4)?.status = Disc.STATE_BLACK
        field.getDisc(4, 5)?.status = Disc.STATE_BLACK
        field.getDisc(5, 5)?.status = Disc.STATE_WHITE
        status = STATE_PLAYING

        while (true) {
            var disc: Disc
            while (true) {
                disc = getDiscByInput()
                if (!canSet(disc, PLAYER_COLOR)) {
                    print("You cannot set disc here")
                } else {
                    break
                }
            }
            setDisc(disc, PLAYER_COLOR)
            setDisc(cpu.getNextDisc(), CPU_COLOR)
            reloadView()
        }
    }

    fun judge(): Boolean {
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
                        return false
                    }
                }
            }
        }
        val winner = if (black > white) {
            "Black win!"
        } else if (white > black) {
            "White win!"
        } else {
            "Draw!"
        }
        print("$winner (Black: $black, White: $white)")
        return true
    }

    fun print(message: String) {
        reloadView(message)
    }

    fun waitInput(message: String): String {
        reloadView("", message)
        return readLine()!!
    }

    fun getDiscByInput(): Disc {
        var x: Int
        while (true) {
            val i = waitInput("Please enter x coordinate").toIntOrNull()
            if (i is Int && i in 1..8) {
                x = i
                break
            }
        }
        var y: Int
        while (true) {
            val i = waitInput("Please enter y coordinate").toIntOrNull()
            if (i is Int && i in 1..8) {
                y = i
                break
            }
        }
        return field.getDisc(x, y)!!
    }

    fun canSet(disc: Disc, color: Int): Boolean {
        if (disc.status != Disc.STATE_EMPTY) {
            return false
        }
        around.forEach {
            var x = disc.x + it.first
            var y = disc.y + it.second
            while (true) {
                val disc2 = field.getDisc(x, y)
                if (disc2 !is Disc) {
                    return@forEach
                }
                if (disc2.status == Disc.STATE_EMPTY) {
                    return@forEach
                }
                if (disc2.status == color) {
                    return true
                }
                x += it.first
                y += it.second
            }
        }
        return false
    }

    fun setDisc(disc: Disc, color: Int) {
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

    private fun reloadView(message: String = "", inputMessage: String = "") {
        var s = ""
        /*
        var count = 0
        while(count < lastOutput.count()) {
            s += "\b"
            count++
        }
         */
        s += "Player: black(○)\n  1 2 3 4 5 6 7 8 x\n"
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
        if (message.isNotEmpty()) {
            s += "- $message\n"
        }
        if (inputMessage.isNotEmpty()) {
            s += "$inputMessage: "
        }
        kotlin.io.print(s)
    }
}