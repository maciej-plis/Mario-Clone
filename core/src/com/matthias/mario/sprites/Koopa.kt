package com.matthias.mario.sprites

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.Fixture
import com.matthias.mario.common.*
import com.matthias.mario.extensions.findRegions
import com.matthias.mario.extensions.flipToDirection
import com.matthias.mario.extensions.setCenter
import com.matthias.mario.extensions.toMeters
import com.matthias.mario.screens.GameScreen
import ktx.box2d.body
import ktx.box2d.box
import ktx.box2d.polygon
import kotlin.experimental.or

const val KOOPA_BODY = "koopa-body"

val KOOPA_COLLISION_CATEGORY = ENEMY_BIT
val KOOPA_COLLISION_MASK = GROUND_BIT or BRICK_BIT or MYSTERY_BLOCK_BIT or MARIO_BIT or ENEMY_BIT

class Koopa(gameScreen: GameScreen, initialPosition: Vector2) : Enemy(gameScreen) {

    private val walkAnimation: Animation<TextureRegion>

    override var body = defineBody(initialPosition)
    override var fixtures = defineFixtures()

    init {
        val walkTextures = enemiesAtlas.findRegions("koopa-walk-1", "koopa-walk-2")
        walkAnimation = Animation<TextureRegion>(0.3f, walkTextures)

        setRegion(walkTextures.first())
        setSize(walkTextures.first().regionWidth.toMeters(), walkTextures.first().regionHeight.toMeters())
        setOriginCenter()
        setCenter(body.x, body.y)

        gameScreen.enemies.add(this)
    }

    override fun update(delta: Float) {
        updateState(delta)
        updateTexture()
        setCenter(body.position)
    }

    override fun onHeadHit() {
        TODO("Not yet implemented")
    }

    private fun defineBody(initialPosition: Vector2): Body {
        return gameScreen.world.body(type = DynamicBody) {
            position.set(initialPosition.x.toMeters(), initialPosition.y.toMeters())
            userData = this@Koopa
        }
    }

    private fun defineFixtures(): MutableMap<String, Fixture> {
        val fixtures: MutableMap<String, Fixture> = mutableMapOf()
        fixtures[KOOPA_BODY] = body.box(width = 12f.toMeters(), height = 20f.toMeters()) {
            filter.categoryBits = KOOPA_COLLISION_CATEGORY
            filter.maskBits = KOOPA_COLLISION_MASK
            userData = KOOPA_BODY
        }
        return fixtures;
    }

    private fun updateState(delta: Float) {
        stateTimer += delta
    }

    private fun updateTexture() {
        frame.flipToDirection(direction)
        setRegion(frame)
    }

    private val frame: TextureRegion
        get() = walkAnimation.getKeyFrame(stateTimer, true)
}