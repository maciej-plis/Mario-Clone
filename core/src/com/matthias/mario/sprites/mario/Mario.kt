package com.matthias.mario.sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP
import com.badlogic.gdx.graphics.g2d.Sprite
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
import com.matthias.mario.sprites.mario.MarioSmallState
import com.matthias.mario.sprites.mario.MarioState
import ktx.box2d.body
import ktx.box2d.box
import ktx.box2d.edge
import kotlin.experimental.or

enum class State { STANDING, RUNNING, JUMPING, FALLING, GROWING, SHRINKING }
enum class MarioFixtures { HEAD, BODY, FEET }

const val MARIO_HEAD = "mario-head"
const val MARIO_BODY = "mario-body"
const val MARIO_FEET = "mario-feet"

val MARIO_POSITION = Vector2(64f, 64f)
val MARIO_COLLISION_CATEGORY = MARIO_BIT
val MARIO_COLLISION_MASK = GROUND_BIT or BRICK_BIT or MYSTERY_BLOCK_BIT or ENEMY_BIT or OBJECT_BIT or ITEM_BIT

class Mario(private val gameScreen: GameScreen) : Sprite() {

    private val marioState: MarioState = MarioSmallState(gameScreen, this)

    private val frame: TextureRegion
        get() = marioState.frame.inDirection(direction)

    private var direction: XDirection = RIGHT

    val body = defineBody()
    val fixtures = defineFixtures()

    init {
        setRegion(frame)
        setSize(frame.regionWidth.toMeters(), frame.regionHeight.toMeters())
        setOriginCenter()
        setCenter(body.x, body.y)
    }

    fun handleInput(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Keys.UP) && body.linearVelocity.y == 0f) {
            marioState.jump()
        }

        if (Gdx.input.isKeyPressed(Keys.LEFT) && body.linearVelocity.x >= -2f) {
            marioState.moveLeft()
        }

        if (Gdx.input.isKeyPressed(Keys.RIGHT) && body.linearVelocity.x <= 2f) {
            marioState.moveRight()
        }
    }

    fun update(delta: Float) {
        marioState.update(delta)
        updateDirection()
        setRegion(frame)
        setCenter(body.position)
    }

    private fun defineBody(): Body {
        return gameScreen.world.body(type = DynamicBody) {
            position.set(MARIO_POSITION.x.toMeters(), MARIO_POSITION.y.toMeters())
            userData = this@Mario
        }
    }

    private fun defineFixtures(): MutableMap<String, Fixture> {
        val fixtures: MutableMap<String, Fixture> = mutableMapOf()
        fixtures[MARIO_BODY] = body.box(10f.toMeters(), 11f.toMeters()) {
            filter.categoryBits = MARIO_COLLISION_CATEGORY
            filter.maskBits = MARIO_COLLISION_MASK
            userData = MARIO_BODY
        }
        fixtures[MARIO_FEET] = body.edge(Vector2((-2f).toMeters(), (-6f).toMeters()), Vector2(2f.toMeters(), (-6f).toMeters())) {
            filter.categoryBits = MARIO_COLLISION_CATEGORY
            filter.maskBits = MARIO_COLLISION_MASK
            userData = MARIO_FEET
        }
        fixtures[MARIO_HEAD] = body.edge(Vector2((-2).toMeters(), 6.toMeters()), Vector2(2f.toMeters(), 6.toMeters())) {
            filter.categoryBits = MARIO_COLLISION_CATEGORY
            filter.maskBits = MARIO_COLLISION_MASK
            userData = MARIO_HEAD
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
}