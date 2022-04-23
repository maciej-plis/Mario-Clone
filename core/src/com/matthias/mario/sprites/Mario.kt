package com.matthias.mario.sprites

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.EdgeShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.matthias.mario.MarioGame.Companion.PPM
import com.matthias.mario.common.createBodyWithFixtures
import com.matthias.mario.common.findRegions
import com.matthias.mario.common.getAtlas
import com.matthias.mario.screens.GameScreen
import com.matthias.mario.sprites.Mario.State.*
import com.matthias.mario.sprites.Mario.XDirection.LEFT
import com.matthias.mario.sprites.Mario.XDirection.RIGHT

class Mario(private val gameScreen: GameScreen) :
    Sprite(gameScreen.assetManager.getAtlas("mario.atlas").findRegion("mario-stand")) {

    enum class State { STANDING, RUNNING, JUMPING, FALLING }
    enum class XDirection { LEFT, RIGHT }

    private val marioAtlas: TextureAtlas = gameScreen.assetManager.getAtlas("mario.atlas")
    private val standTexture: TextureRegion
    private val runAnimation: Animation<TextureRegion>
    private val jumpAnimation: Animation<TextureRegion>

    val body: Body = defineBody()

    var currentState: State = STANDING
    var previousState: State? = null
    var xDirection: XDirection = RIGHT

    var stateTimer: Float = 0f


    init {
        setBounds(body.position.x / PPM, body.position.y / PPM, width / PPM, height / PPM)
        standTexture = marioAtlas.findRegion("mario-stand")//.apply(scaleTextureRegion)

        val runTextures = marioAtlas.findRegions("mario-run-1", "mario-run-2", "mario-run-3")
        runAnimation = Animation<TextureRegion>(0.1f, runTextures)

        val jumpTextures = marioAtlas.findRegions("mario-jump-1", "mario-jump-2")
        jumpAnimation = Animation<TextureRegion>(0.1f, jumpTextures)
    }

    fun update(delta: Float) {
        updateDirection()
        updateState(delta)
        updateTexture()
        setPosition(body.position.x - width / 2, body.position.y - height / 2)
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
            body.linearVelocity.y > 0 || (body.linearVelocity.y < 0 && previousState == JUMPING) -> JUMPING
            body.linearVelocity.y < 0 -> FALLING
            body.linearVelocity.x != 0f -> RUNNING
            else -> STANDING
        }
        stateTimer = if (currentState == previousState) stateTimer + delta else 0f
    }

    private fun updateTexture() {
        val frame = frame.apply {
            if((xDirection == LEFT && !isFlipX) || (xDirection == RIGHT && isFlipX)) {
                flip(true, false)
            }
        }
        setRegion(frame)
    }

    private val frame: TextureRegion
        get() = when (currentState) {
            STANDING, FALLING -> standTexture
            RUNNING -> runAnimation.getKeyFrame(stateTimer, true)
            JUMPING -> jumpAnimation.getKeyFrame(stateTimer)
        }

    private fun defineBody(): Body {
        val bodyDef = BodyDef().apply {
            type = DynamicBody
            position.set(64f / PPM, 64f / PPM)
        }
        val fixtureDef1 = FixtureDef().apply {
            shape = CircleShape().apply { radius = 6f / PPM }
        }
        val fixtureDef2 = FixtureDef().apply {
            shape = EdgeShape().apply { set(Vector2(-2f / PPM, -6f / PPM), Vector2(2f / PPM, -6f / PPM)) }
        }
        return gameScreen.world.createBodyWithFixtures(bodyDef, fixtureDef1, fixtureDef2)
    }
}