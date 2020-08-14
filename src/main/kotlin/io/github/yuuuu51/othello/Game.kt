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

    private val field = Field()

    private val cpu = CPU(this)

    private var lastOutput = ""

    private var lastPrint = ""

    fun start() {
        if (status != STATE_WAITING) {
            return
        }
        field.getDisc(4, 4)?.status = Disc.STATE_WHITE
        field.getDisc(5, 4)?.status = Disc.STATE_BLACK
        field.getDisc(4, 5)?.status = Disc.STATE_BLACK
        field.getDisc(5, 5)?.status = Disc.STATE_WHITE
        reloadView()
        status = STATE_PLAYING

        while (true) {
            var playerDisc: Disc = getDiscByInput()
            while (true) {
                if (!canSet(playerDisc, PLAYER_COLOR)) {
                    print("You cannot set disc here")
                } else {
                    break
                }
                playerDisc = getDiscByInput()
            }
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
        lastPrint = message
    }

    fun waitInput(message: String): String {
        reloadView(lastPrint, message)
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

        return true
    }

    fun setDisc(disc: Disc, color: Int) {
        require(canSet(disc, color))
    }

    private fun reloadView(message: String = "", inputMessage: String = "") {
        var s = ""
        var count = 0
        while(count < lastOutput.count()) {
            s += "\b"
            count++
        }
        s += "Player: black\n  1 2 3 4 5 6 7 8 \n"
        var key = 0
        for (y in 1..8) {
            s += y
            for (x in 1..8) {
                val disc = field.getDisc(x, y)
                val view = when (disc?.status) {
                    Disc.STATE_BLACK -> {
                        "●"
                    }
                    Disc.STATE_WHITE -> {
                        "○"
                    }
                    else -> {
                        "x"
                    }
                }
                s += " $view"
                key++
            }
            s += "\n"
        }
        if (message.isNotEmpty()) {
            s += "- $message\n"
        }
        if (inputMessage.isNotEmpty()) {
            s += "$inputMessage: "
        }
        kotlin.io.print(s)
        lastOutput = s
    }
}