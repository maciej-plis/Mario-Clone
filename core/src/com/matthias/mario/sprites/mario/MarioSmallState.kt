package com.matthias.mario.sprites.mario

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.matthias.mario.common.*
import com.matthias.mario.extensions.getAtlas
import com.matthias.mario.extensions.getSound
import com.matthias.mario.screens.GameScreen
import com.matthias.mario.sprites.Mario
import com.matthias.mario.sprites.State
import com.matthias.mario.sprites.State.*
import com.matthias.mario.sprites.mario.MarioStateId.SMALL_MARIO

class MarioSmallState(private val gameScreen: GameScreen, private val mario: Mario) : MarioState {

    companion object {
        val MOVE_LEFT_VECTOR = Vector2(-0.1f, 0f)
        val MOVE_RIGHT_VECTOR = Vector2(0.1f, 0f)
        val JUMP_VECTOR = Vector2(0f, 4f)
    }

    private val state = EntityState(STANDING)
    private val textures = EntityTextures<State>(gameScreen.assetManager.getAtlas(MARIO_ATLAS)).apply {
        addTexture(STANDING, "small-mario-stand")
        addTexture(FALLING, "small-mario-fall")
        addTexture(JUMPING, "small-mario-jump")
        addAnimation(RUNNING, 0.1f, *enumerateString("small-mario-run-", 1, 3), mode = LOOP)
        addAnimation(GROWING, 0.1f, *enumerateString("mario-grow-", 1, 10))
        addAnimation(SHRINKING, 0.1f, *enumerateString("mario-shrink-", 1, 2))
    }

    override val stateId = SMALL_MARIO

    override val frame: TextureRegion
        get() = textures.getCurrentTexture(state)

    override fun moveLeft() {
        mario.body.applyLinearImpulse(MOVE_LEFT_VECTOR, mario.body.worldCenter, true)
    }

    override fun moveRight() {
        mario.body.applyLinearImpulse(MOVE_RIGHT_VECTOR, mario.body.worldCenter, true)
    }

    override fun jump() {
        mario.body.applyLinearImpulse(JUMP_VECTOR, mario.body.worldCenter, true)
        gameScreen.assetManager.getSound(JUMP_SMALL_SOUND).play()
    }

    override fun update(delta: Float) {
        updateState(delta)
    }

    private fun updateState(delta: Float) {
        val currentState = when {
            mario.body.linearVelocity.y != 0f -> JUMPING
            mario.body.linearVelocity.x != 0f -> RUNNING
            else -> STANDING
        }
        state.update(currentState, delta)
    }
}