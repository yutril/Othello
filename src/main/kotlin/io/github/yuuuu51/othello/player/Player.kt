package io.github.yuuuu51.othello.player

import io.github.yuuuu51.othello.Disc

interface Player {

    val color: Int

    fun getNextDisc(): Disc

    fun getName(): String
}