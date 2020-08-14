package io.github.yuuuu51.othello

class Field {

    private val discs = mutableListOf<Disc>()

    init {
        for (count in 1..64) {
            discs.add(Disc())
        }
    }

    fun getDisc(x: Int, y: Int): Disc? {
        if (x !in 1..8 || y !in 1..8) {
            return null
        }
        return discs[(y - 1) * 8 + x - 1]
    }
}