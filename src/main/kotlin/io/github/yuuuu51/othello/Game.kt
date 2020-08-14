package io.github.yuuuu51.othello

class Game {

    companion object {
        const val STATE_WAITING = 0
        const val STATE_PLAYING = 1
        const val STATE_FINISH = 2
    }

    var status = STATE_WAITING
        private set

    private val field = Field()

    private var lastOutput = ""

    fun start() {
        if (status != STATE_WAITING) {
            return
        }
        field.getDisc(4, 4).status = Disc.STATE_WHITE
        field.getDisc(5, 4).status = Disc.STATE_BLACK
        field.getDisc(4, 5).status = Disc.STATE_BLACK
        field.getDisc(5, 5).status = Disc.STATE_WHITE
        reloadView()
        status = STATE_PLAYING
    }

    fun reloadView() {
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
                val view = when (disc.status) {
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
        print(s)
        lastOutput = s
    }
}