package io.github.yuuuu51.othello

class CPU(private val game: Game) {

    fun getNextDisc(): Disc {
        val discs = mutableListOf<Disc>()
        game.field.getAllDisc().forEach { (_, map) ->
            map.forEach { (_, disc) ->
                if (game.canSet(disc, Game.CPU_COLOR)) {
                    discs.add(disc)
                }
            }
        }
        return discs.random()
    }
}