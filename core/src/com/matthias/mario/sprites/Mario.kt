package com.matthias.mario.sprites

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.Fixture
import com.matthias.mario.common.*
import com.matthias.mario.screens.GameScreen
import com.matthias.mario.sprites.Mario.State.*
import com.matthias.mario.sprites.Mario.XDirection.LEFT
import com.matthias.mario.sprites.Mario.XDirection.RIGHT
import ktx.box2d.body
import ktx.box2d.circle
import ktx.box2d.edge
import kotlin.experimental.or

class Mario(private val gameScreen: GameScreen) : Sprite() {

    companion object {
        val INITIAL_POSITION = Vector2(64f, 64f)
    }

    enum class State { STANDING, RUNNING, JUMPING, FALLING }
    enum class XDirection { LEFT, RIGHT }

    private val marioAtlas: TextureAtlas = gameScreen.assetManager.getAtlas("mario.atlas")
    private val standTexture: TextureRegion

    //    private val fallTexture: TextureRegion
    private val runAnimation: Animation<TextureRegion>
    private val jumpAnimation: Animation<TextureRegion>

    val fixtures: MutableMap<String, Fixture> = mutableMapOf()
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
        val body = gameScreen.world.body(type = DynamicBody) { position.set(INITIAL_POSITION.x.toMeters(), INITIAL_POSITION.y.toMeters()) }
        fixtures["body"] = body.circle(radius = 6f.toMeters()) {
            filter.categoryBits = MARIO_BIT
            filter.maskBits = DEFAULT_BIT or BRICK_BIT or MYSTERY_BLOCK_BIT
        }
//        fixtures["feet"] = body.edge(Vector2((-2f).toMeters(), (-6f).toMeters()), Vector2(2f.toMeters(), (-6f).toMeters()))
        fixtures["head"] = body.edge(Vector2((-2).toMeters(), 6.toMeters()), Vector2(2f.toMeters(), 6.toMeters())) {
            isSensor = true
            userData = "head"
        }
        return body
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
        val facingRightButFlipped = xDirection == RIGHT && isFlipX
        if (facingLeftButNotFlipped || facingRightButFlipped) {
            flip(true, false)
        }
    }
}