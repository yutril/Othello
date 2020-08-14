package io.github.yuuuu51.othello

class Disc {

    companion object {
        const val STATE_EMPTY = 0
        const val STATE_BLACK = 1
        const val STATE_WHITE = 2
    }

    var status: Int = STATE_EMPTY
}