package com.matthias.mario.sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.Fixture
import com.matthias.mario.common.*
import com.matthias.mario.extensions.*
import com.matthias.mario.extensions.XDirection.LEFT
import com.matthias.mario.extensions.XDirection.RIGHT
import com.matthias.mario.screens.GameScreen
import com.matthias.mario.sprites.State.*
import ktx.box2d.body
import ktx.box2d.circle
import ktx.box2d.edge
import kotlin.experimental.or

enum class State { STANDING, RUNNING, JUMPING, FALLING }

const val HEAD_FIXT = "head"
const val BODY_FIXT = "body"
const val FEET_FIXT = "feet"

const val COLLISION_CATEGORY = MARIO_BIT
val COLLISION_MASK = GROUND_BIT or BRICK_BIT or MYSTERY_BLOCK_BIT

val INITIAL_POSITION = Vector2(64f, 64f)

class Mario(private val gameScreen: GameScreen) : Sprite() {

    private val marioAtlas: TextureAtlas = gameScreen.assetManager.getAtlas(MARIO_ATLAS)

    private val standTexture: TextureRegion
//    private val fallTexture: TextureRegion
    private val runAnimation: Animation<TextureRegion>
    private val jumpAnimation: Animation<TextureRegion>

    val body = defineBody()
    val fixtures = defineFixtures()

    var currentState: State = STANDING
    var previousState: State? = null
    var stateTimer: Float = 0f

    var direction: XDirection = RIGHT
    var inAir: Boolean = false

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

    fun handleInput(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && body.linearVelocity.y == 0f) {
            body.applyLinearImpulse(Vector2(0f, 4f), body.worldCenter, true)
            gameScreen.assetManager.getSound(JUMP_SMALL_SOUND).play()
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && body.linearVelocity.x >= -2f) {
            body.applyLinearImpulse(Vector2(-0.1f, 0f), body.worldCenter, true)
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && body.linearVelocity.x <= 2f) {
            body.applyLinearImpulse(Vector2(0.1f, 0f), body.worldCenter, true)
        }
    }

    fun update(delta: Float) {
        updateDirection()
        updateState(delta)
        updateTexture()
        setCenter(body.position)
    }

    private fun defineBody(): Body {
        return gameScreen.world.body(type = DynamicBody) { position.set(INITIAL_POSITION.x.toMeters(), INITIAL_POSITION.y.toMeters()) }
    }

    private fun defineFixtures(): MutableMap<String, Fixture> {
        val fixtures: MutableMap<String, Fixture> = mutableMapOf()
        fixtures[BODY_FIXT] = body.circle(radius = 6f.toMeters()) {
            filter.categoryBits = COLLISION_CATEGORY
            filter.maskBits = COLLISION_MASK
            userData = BODY_FIXT
        }
        fixtures[FEET_FIXT] = body.edge(Vector2((-2f).toMeters(), (-6f).toMeters()), Vector2(2f.toMeters(), (-6f).toMeters())) {
            filter.categoryBits = COLLISION_CATEGORY
            filter.maskBits = COLLISION_MASK
            userData = FEET_FIXT
        }
        fixtures[HEAD_FIXT] = body.edge(Vector2((-2).toMeters(), 6.toMeters()), Vector2(2f.toMeters(), 6.toMeters())) {
            filter.categoryBits = COLLISION_CATEGORY
            filter.maskBits = COLLISION_MASK
            userData = HEAD_FIXT
            isSensor = true
        }
        return fixtures
    }

    private fun updateDirection() {
        direction = when {
            body.linearVelocity.x > 0 -> RIGHT
            body.linearVelocity.x < 0 -> LEFT
            else -> direction
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
        setRegion(frame.apply { flipToDirection(direction) })
    }

    private val frame: TextureRegion
        get() = when (currentState) {
            STANDING, FALLING -> standTexture
            RUNNING -> runAnimation.getKeyFrame(stateTimer, true)
            JUMPING -> jumpAnimation.getKeyFrame(stateTimer)
        }
}