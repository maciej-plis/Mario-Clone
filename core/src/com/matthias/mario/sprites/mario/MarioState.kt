package com.matthias.mario.sprites.mario

import com.badlogic.gdx.graphics.g2d.TextureRegion

enum class MarioStateId {
    SMALL_MARIO, BIG_MARIO
}

interface MarioState {

    val stateId: MarioStateId
    val frame: TextureRegion

    fun moveLeft()
    fun moveRight()
    fun jump()

    fun update(delta: Float)
}