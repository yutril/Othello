package io.github.yuuuu51.othello

class Field {

    private val discs = mutableMapOf<Int, Map<Int, Disc>>()

    init {
        for (x in 1..8) {
            val map = mutableMapOf<Int, Disc>()
            for (y in 1..8) {
                map[y] = Disc(x, y)
            }
            discs[x] = map
        }
    }

    fun getDisc(x: Int, y: Int): Disc? {
        if (x !in 1..8 || y !in 1..8) {
            return null
        }
        return discs[x]?.get(y)
    }

    fun getAllDisc(): Map<Int, Map<Int, Disc>> {
        return discs
    }
}