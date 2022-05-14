package com.matthias.mario.sprites.items

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.matthias.mario.common.*
import com.matthias.mario.extensions.toMeters
import com.matthias.mario.screens.GameScreen
import com.matthias.mario.sprites.items.Mushroom.Companion.MushroomFixtures.BODY
import com.matthias.mario.sprites.mario.Mario
import ktx.box2d.body
import ktx.box2d.box
import kotlin.experimental.or

class Mushroom(gameScreen: GameScreen, initialPosition: Vector2) : Item(gameScreen) {

    companion object {
        enum class MushroomFixtures { BODY }
        val MUSHROOM_COLLISION_MASK: Short = GROUND_BIT or BRICK_BIT or MYSTERY_BLOCK_BIT or MARIO_BIT or ENEMY_BIT or OBJECT_BIT or MARIO_BIT
    }

    private val mushroomTexture: TextureRegion

    override var body: Body = defineBody(initialPosition)
    override var fixtures: FixturesMap<MushroomFixtures> = defineFixtures()

    init {
        velocity.set(0.5f, 0f)
        mushroomTexture = itemsAtlas.findRegion("mushroom")

        setRegion(mushroomTexture)
        setSize(mushroomTexture.regionWidth.toMeters(), mushroomTexture.regionHeight.toMeters())
        setOriginCenter()
        setCenter(body.x, body.y)
    }

    override fun update() {
        body.linearVelocity= Vector2(velocity.x, body.linearVelocity.y)
        setCenter(body.x, body.y)
    }

    override fun use(mario: Mario) {
        mario.grow()
        Gdx.app.postRunnable {
            gameScreen.items.remove(this)
            gameScreen.world.destroyBody(body)
        }
    }

    private fun defineBody(initialPosition: Vector2): Body {
        return gameScreen.world.body(type = DynamicBody) {
            position.set(initialPosition.x.toMeters(), initialPosition.y.toMeters())
            userData = this@Mushroom
        }
    }

    private fun defineFixtures(): FixturesMap<MushroomFixtures> {
        val fixtures: FixturesMap<MushroomFixtures> = mutableMapOf()
        fixtures[BODY] = body.box(width = 12f.toMeters(), height = 12f.toMeters()) {
            filter.categoryBits = ITEM_BIT
            filter.maskBits = MUSHROOM_COLLISION_MASK
            userData = BODY
        }
        return fixtures
    }
}