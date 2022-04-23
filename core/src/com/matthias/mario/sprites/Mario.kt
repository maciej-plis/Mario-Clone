package com.matthias.mario.sprites

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.matthias.mario.MarioGame.Companion.PPM
import com.matthias.mario.common.*
import com.matthias.mario.screens.GameScreen
import com.matthias.mario.sprites.Mario.State.*
import com.matthias.mario.sprites.Mario.XDirection.LEFT
import com.matthias.mario.sprites.Mario.XDirection.RIGHT
import ktx.box2d.body
import ktx.box2d.circle
import ktx.box2d.edge

class Mario(private val gameScreen: GameScreen) : Sprite() {

    enum class State { STANDING, RUNNING, JUMPING, FALLING }
    enum class XDirection { LEFT, RIGHT }

    private val marioAtlas: TextureAtlas = gameScreen.assetManager.getAtlas("mario.atlas")
    private val standTexture: TextureRegion

    //    private val fallTexture: TextureRegion
    private val runAnimation: Animation<TextureRegion>
    private val jumpAnimation: Animation<TextureRegion>

    val body: Body = defineBody()

    var currentState: State = STANDING
    var previousState: State? = null
    var xDirection: XDirection = RIGHT

    var stateTimer: Float = 0f


    init {
        standTexture = marioAtlas.findRegion("mario-stand")
//        fallTexture = marioAtlas.findRegion("mario-fall")

        val runTextures = marioAtlas.findRegions("mario-run-1", "mario-run-2", "mario-run-3")
        runAnimation = Animation<TextureRegion>(0.1f, runTextures)

        val jumpTextures = marioAtlas.findRegions("mario-jump-1", "mario-jump-2")
        jumpAnimation = Animation<TextureRegion>(0.1f, jumpTextures)

        setRegion(standTexture)
        setSize(standTexture.regionWidth.toMeters(), standTexture.regionHeight.toMeters())
        setOriginCenter()
        setCenter(body.x.toMeters(), body.y.toMeters())
    }

    fun update(delta: Float) {
        updateDirection()
        updateState(delta)
        updateTexture()
        setCenter(body.position)
    }

    private fun defineBody(): Body {
        return gameScreen.world.body(type = DynamicBody) {
            position.set(64f / PPM, 64f / PPM)
            circle(radius = 6f / PPM)
            edge(Vector2(-2f / PPM, -6f / PPM), Vector2(2f / PPM, -6f / PPM))
            edge(Vector2(-2 / PPM, 6 / PPM), Vector2(2f / PPM, 6 / PPM))
        }
    }

    private fun updateDirection() {
        xDirection = when {
            body.linearVelocity.x > 0 -> RIGHT
            body.linearVelocity.x < 0 -> LEFT
            else -> xDirection
        }
    }

    private fun updateState(delta: Float) {
        previousState = currentState
        currentState = when {
            body.linearVelocity.y != 0f -> JUMPING
            body.linearVelocity.x != 0f -> RUNNING
            else -> STANDING
        }
        stateTimer = if (currentState == previousState) stateTimer + delta else 0f
    }

    private fun updateTexture() {
        setRegion(frame.apply { flipToDirection(xDirection) })
    }

    private val frame: TextureRegion
        get() = when (currentState) {
            STANDING, FALLING -> standTexture
            RUNNING -> runAnimation.getKeyFrame(stateTimer, true)
            JUMPING -> jumpAnimation.getKeyFrame(stateTimer)
        }

    private fun TextureRegion.flipToDirection(xDirection: XDirection) {
        val facingLeftButNotFlipped = xDirection == LEFT && !isFlipX
        val facingRightButFlipped = xDirection == LEFT && !isFlipX
        if (facingLeftButNotFlipped || facingRightButFlipped) {
            flip(true, false)
        }
    }
}