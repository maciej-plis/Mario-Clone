package com.matthias.mario.sprites.items

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.matthias.mario.common.*
import com.matthias.mario.extensions.toMeters
import com.matthias.mario.screens.GameScreen
import ktx.box2d.body
import ktx.box2d.box
import kotlin.experimental.or

class Flower(gameScreen: GameScreen, initialPosition: Vector2) : Item(gameScreen) {

    companion object {
        const val FLOWER: String = "flower"
        val FLOWER_COLLISION_CATEGORY: Short = ITEM_BIT
        val FLOWER_COLLISION_MASK: Short = GROUND_BIT or BRICK_BIT or MYSTERY_BLOCK_BIT or MARIO_BIT or ENEMY_BIT or OBJECT_BIT or MARIO_BIT
    }

    private val flowerTexture: TextureRegion

    override var body = defineBody(initialPosition)
    override var fixtures = defineFixtures()

    init {
        flowerTexture = itemsAtlas.findRegion("flower")

        setRegion(flowerTexture)
        setSize(flowerTexture.regionWidth.toMeters(), flowerTexture.regionHeight.toMeters())
        setOriginCenter()
        setCenter(body.x, body.y)
    }

    override fun update() {
        body.linearVelocity= Vector2(velocity.x, body.linearVelocity.y)
        setCenter(body.x, body.y)
    }

    override fun use() {
        Gdx.app.postRunnable { gameScreen.items.remove(this) }
        gameScreen.world.destroyBody(body)
    }

    private fun defineBody(initialPosition: Vector2): Body {
        return gameScreen.world.body(type = BodyDef.BodyType.DynamicBody) {
            position.set(initialPosition.x.toMeters(), initialPosition.y.toMeters())
            userData = this@Flower
        }
    }

    private fun defineFixtures(): FixturesMap {
        val fixtures: FixturesMap = mutableMapOf()
        fixtures[FLOWER] = body.box(width = 12f.toMeters(), height = 12f.toMeters()) {
            filter.categoryBits = FLOWER_COLLISION_CATEGORY
            filter.maskBits = FLOWER_COLLISION_MASK
            userData = FLOWER
        }
        return fixtures
    }
}