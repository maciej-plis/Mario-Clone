package com.matthias.mario.sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.Fixture
import com.matthias.mario.common.*
import com.matthias.mario.extensions.findRegions
import com.matthias.mario.extensions.sclToMeters
import com.matthias.mario.extensions.setCenter
import com.matthias.mario.extensions.toMeters
import com.matthias.mario.screens.GameScreen
import com.matthias.mario.sprites.Goomba.State.SQUASHED
import com.matthias.mario.sprites.Goomba.State.WALKING
import ktx.box2d.body
import ktx.box2d.box
import ktx.box2d.polygon
import kotlin.experimental.or
import kotlin.experimental.xor

const val GOOMBA_HEAD = "goomba-head"
const val GOOMBA_BODY = "goomba-body"

val GOOMBA_COLLISION_CATEGORY = ENEMY_BIT
val GOOMBA_COLLISION_MASK = GROUND_BIT or BRICK_BIT or MYSTERY_BLOCK_BIT or MARIO_BIT or ENEMY_BIT

val headVertices = arrayOf(
    Vector2(-5f, 8f).sclToMeters(),
    Vector2(5f, 8f).sclToMeters(),
    Vector2(-3f, 3f).sclToMeters(),
    Vector2(3f, 3f).sclToMeters()
)

class Goomba(gameScreen: GameScreen, initialPosition: Vector2) : Enemy(gameScreen) {

    enum class State { WALKING, SQUASHED }

    private val walkAnimation: Animation<TextureRegion>
    private val squashedTexture: TextureRegion

    override var body = defineBody(initialPosition)
    override var fixtures = defineFixtures()

    var currentState = WALKING
    var previousState = currentState
    var velocityX = -0.5f

    init {
        val walkTextures = enemiesAtlas.findRegions("goomba-walk-1", "goomba-walk-2")
        walkAnimation = Animation<TextureRegion>(0.3f, walkTextures)
        squashedTexture = enemiesAtlas.findRegion("goomba-squashed")

        setRegion(walkTextures.first())
        setSize(walkTextures.first().regionWidth.toMeters(), walkTextures.first().regionHeight.toMeters())
        setOriginCenter()
        setCenter(body.x, body.y)

        gameScreen.enemies.add(this)
    }

    override fun draw(batch: Batch?) {
        if(currentState == SQUASHED && stateTimer >= 0.25) {
            return
        }
        super.draw(batch)
    }

    override fun update(delta: Float) {
        body.linearVelocity= Vector2(velocityX, body.linearVelocity.y)
        updateState(delta)
        updateTexture()
        setCenter(body.position)
        setOriginCenter()
    }

    override fun onHeadHit() {
        if(currentState == SQUASHED) {
            return
        }

        currentState = SQUASHED
        stateTimer = 0f

        velocityX = 0f
        setSize(squashedTexture.regionWidth.toMeters(), squashedTexture.regionHeight.toMeters())

        Gdx.app.postRunnable {
            body.destroyFixture(fixtures[GOOMBA_HEAD])
            body.destroyFixture(fixtures[GOOMBA_BODY])
            fixtures[GOOMBA_BODY] = body.box(width = 12f.toMeters(), height = 5f.toMeters()) {
                filter.categoryBits = GOOMBA_COLLISION_CATEGORY
                filter.maskBits = GOOMBA_COLLISION_MASK xor MARIO_BIT
                userData = GOOMBA_BODY
            }
        }

    }

    private fun defineBody(initialPosition: Vector2): Body {
        return gameScreen.world.body(type = DynamicBody) {
            position.set(initialPosition.x.toMeters(), initialPosition.y.toMeters())
            userData = this@Goomba
        }
    }

    private fun defineFixtures(): MutableMap<String, Fixture> {
        val fixtures: MutableMap<String, Fixture> = mutableMapOf()
        fixtures[GOOMBA_HEAD] = body.polygon(vertices = headVertices) {
            filter.categoryBits = GOOMBA_COLLISION_CATEGORY
            filter.maskBits = GOOMBA_COLLISION_MASK
            userData = GOOMBA_HEAD
            restitution = 0.4f
        }
        fixtures[GOOMBA_BODY] = body.box(width = 12f.toMeters(), height = 12f.toMeters()) {
            filter.categoryBits = GOOMBA_COLLISION_CATEGORY
            filter.maskBits = GOOMBA_COLLISION_MASK
            userData = GOOMBA_BODY
        }
        return fixtures;
    }

    private fun updateState(delta: Float) {
        previousState = currentState
        stateTimer = if (currentState == previousState) stateTimer + delta else 0f
    }

    private fun updateTexture() {
        setRegion(frame)
    }

    private val frame: TextureRegion
        get() =  when (currentState) {
            WALKING -> walkAnimation.getKeyFrame(stateTimer, true)
            SQUASHED -> squashedTexture
        }
}