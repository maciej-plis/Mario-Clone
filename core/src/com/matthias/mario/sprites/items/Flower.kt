package com.matthias.mario.sprites.items

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.matthias.mario.common.*
import com.matthias.mario.extensions.toMeters
import com.matthias.mario.screens.GameScreen
import com.matthias.mario.sprites.items.Flower.Companion.FlowerFixtures.BODY
import com.matthias.mario.sprites.mario.Mario
import ktx.box2d.body
import ktx.box2d.box
import kotlin.experimental.or

class Flower(gameScreen: GameScreen, initialPosition: Vector2) : Item(gameScreen) {

    companion object {
        enum class FlowerFixtures { BODY }

        val FLOWER_COLLISION_MASK: Short = GROUND_BIT or BRICK_BIT or MYSTERY_BLOCK_BIT or MARIO_BIT or ENEMY_BIT or OBJECT_BIT or MARIO_BIT
    }

    private val flowerTexture: TextureRegion

    override var body: Body = defineBody(initialPosition)
    override var fixtures: FixturesMap<FlowerFixtures> = defineFixtures()

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

    override fun use(mario: Mario) {
        Gdx.app.postRunnable { gameScreen.items.remove(this) }
        Gdx.app.postRunnable { gameScreen.world.destroyBody(body) }
    }

    private fun defineBody(initialPosition: Vector2): Body {
        return gameScreen.world.body(type = BodyDef.BodyType.DynamicBody) {
            position.set(initialPosition.x.toMeters(), initialPosition.y.toMeters())
            userData = this@Flower
        }
    }

    private fun defineFixtures(): FixturesMap<FlowerFixtures> {
        val fixtures: FixturesMap<FlowerFixtures> = mutableMapOf()
        fixtures[BODY] = body.box(width = 12f.toMeters(), height = 12f.toMeters()) {
            filter.categoryBits = ITEM_BIT
            filter.maskBits = FLOWER_COLLISION_MASK
            userData = BODY
        }
        return fixtures
    }
}