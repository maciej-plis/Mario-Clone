package com.matthias.mario.sprites.mario

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.matthias.mario.common.*
import com.matthias.mario.extensions.*
import com.matthias.mario.extensions.XDirection.LEFT
import com.matthias.mario.extensions.XDirection.RIGHT
import com.matthias.mario.screens.GameScreen
import com.matthias.mario.sprites.mario.MarioState.Companion.MarioStateId.BIG_MARIO
import com.matthias.mario.sprites.mario.MarioState.Companion.MarioStateId.SMALL_MARIO
import kotlin.experimental.or

class Mario(val gameScreen: GameScreen) : Sprite() {

    companion object {
        enum class State { STANDING, RUNNING, JUMPING, FALLING, GROWING, SHRINKING }
        enum class MarioFixtures { HEAD, BODY, FEET }

        val MARIO_INITIAL_POSITION = Vector2(64f, 64f).sclToMeters()
        val SMALL_MARIO_SIZE = Vector2(10f, 11f).sclToMeters()
        val BIG_MARIO_SIZE = Vector2(10f, 27f).sclToMeters()
        val MARIO_COLLISION_MASK = GROUND_BIT or BRICK_BIT or MYSTERY_BLOCK_BIT or ENEMY_BIT or OBJECT_BIT or ITEM_BIT
    }

    var marioState: MarioState = MarioSmallState(this, MARIO_INITIAL_POSITION)

    private val frame: TextureRegion
        get() = marioState.frame.inDirection(direction)

    private var direction: XDirection = RIGHT

    init {
        setRegion(frame)
        setOriginCenter()
    }

    fun handleInput() {
        if (Gdx.input.isKeyJustPressed(Keys.UP) && marioState.body.linearVelocity.y == 0f) {
            marioState.jump()
        }
        if (Gdx.input.isKeyPressed(Keys.LEFT) && marioState.body.linearVelocity.x >= -2f) {
            marioState.moveLeft()
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT) && marioState.body.linearVelocity.x <= 2f) {
            marioState.moveRight()
        }
    }

    fun update(delta: Float) {
        marioState.update(delta)
        updateDirection()
        setRegion(frame)
        setOriginBasedPosition(marioState.body.position)
    }

    fun grow() {
        if (marioState.stateId != SMALL_MARIO) return
        changeState { MarioGrowingState(this, marioState.body.position.cpy()) }
        gameScreen.assetManager.getSound(MARIO_GROW_SOUND).play()
    }

    fun shrink() {
        if (marioState.stateId != BIG_MARIO) return
        changeState { MarioShrinkingState(this, marioState.body.position.cpy()) }
        gameScreen.assetManager.getSound(PIPE_SOUND).play()
    }

    fun changeState(stateCallback: () -> MarioState) {
        Gdx.app.postRunnable {
            gameScreen.world.destroyBody(marioState.body)
            marioState = stateCallback()
        }
    }

    override fun setRegion(region: TextureRegion) {
        if (region.regionWidth != regionWidth || region.regionHeight != regionHeight) {
            Gdx.app.debug("Mario", "Sprite dimensions changed")
            setBounds(marioState.body.position.x, marioState.body.position.y, frame.regionWidth.toMeters(), frame.regionHeight.toMeters())
            setOriginCenter()
        }
        super.setRegion(region)
    }

    private fun updateDirection() {
        direction = when {
            marioState.body.linearVelocity.x > 0 -> RIGHT
            marioState.body.linearVelocity.x < 0 -> LEFT
            else -> direction
        }
    }
}