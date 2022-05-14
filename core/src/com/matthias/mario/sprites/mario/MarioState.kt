package com.matthias.mario.sprites.mario

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.Body
import com.matthias.mario.common.FixturesMap
import com.matthias.mario.sprites.mario.Mario.Companion.MarioFixtures

interface MarioState {

    companion object {
        enum class MarioStateId { SMALL_MARIO, BIG_MARIO, GROWING_MARIO, SHRINKING_MARIO }
    }

    val stateId: MarioStateId
    val frame: TextureRegion

    val body: Body
    val fixtures: FixturesMap<MarioFixtures>

    fun moveLeft()
    fun moveRight()
    fun jump()

    fun update(delta: Float)
}