package io.github.yuuuu51.othello.player

import io.github.yuuuu51.othello.Disc
import io.github.yuuuu51.othello.Game

class CPUPlayer(
    private val game: Game,
    override val color: Int
) : Player {

    override fun getNextDisc(): Disc {
        val discs = game.getCanSetDiscs(Game.COLOR_WHITE)
        return discs.random()
    }

    override fun getName(): String {
        return "CPU"
    }
}