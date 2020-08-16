package io.github.yuuuu51.othello.player

import io.github.yuuuu51.othello.Disc
import io.github.yuuuu51.othello.Game

class UserPlayer(
    private val game: Game,
    override val color: Int
) : Player {

    override fun getNextDisc(): Disc {
        var disc: Disc
        while (true) {
            val x: Int
            while (true) {
                print("Please enter x coordinate: ")
                val i = readLine()?.toIntOrNull()
                if (i is Int && i in 1..8) {
                    x = i
                    break
                }
            }
            val y: Int
            while (true) {
                print("Please enter y coordinate: ")
                val i = readLine()?.toIntOrNull()
                if (i is Int && i in 1..8) {
                    y = i
                    break
                }
            }
            disc = game.field.getDisc(x, y)!!
            if (!game.canSet(disc, Game.COLOR_BLACK)) {
                println("- You cannot set disc here")
            } else {
                break
            }
        }
        return disc
    }

    override fun getName(): String {
        return "You"
    }
}