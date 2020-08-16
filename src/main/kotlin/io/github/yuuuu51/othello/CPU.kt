package io.github.yuuuu51.othello

class CPU(private val game: Game) {

    fun getNextDisc(): Disc {
        val discs = game.getCanSetDiscs(Game.CPU_COLOR)
        return discs.random()
    }
}