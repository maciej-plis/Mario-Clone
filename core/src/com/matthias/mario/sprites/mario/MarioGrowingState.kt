package com.matthias.mario.sprites.mario

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.StaticBody
import com.matthias.mario.common.*
import com.matthias.mario.extensions.getAtlas
import com.matthias.mario.extensions.height
import com.matthias.mario.extensions.toMeters
import com.matthias.mario.extensions.width
import com.matthias.mario.screens.GameScreen
import com.matthias.mario.sprites.mario.Mario.Companion.BIG_MARIO_SIZE
import com.matthias.mario.sprites.mario.Mario.Companion.MARIO_COLLISION_MASK
import com.matthias.mario.sprites.mario.Mario.Companion.MarioFixtures
import com.matthias.mario.sprites.mario.Mario.Companion.MarioFixtures.BODY
import com.matthias.mario.sprites.mario.Mario.Companion.State
import com.matthias.mario.sprites.mario.Mario.Companion.State.GROWING
import com.matthias.mario.sprites.mario.MarioState.Companion.MarioStateId
import com.matthias.mario.sprites.mario.MarioState.Companion.MarioStateId.GROWING_MARIO
import ktx.box2d.body
import ktx.box2d.box

class MarioGrowingState(private val mario: Mario, private val initialPosition: Vector2) : MarioState {

    private val gameScreen: GameScreen = mario.gameScreen

    private val state = EntityState(GROWING)
    private val textures = EntityTextures<State>(gameScreen.assetManager.getAtlas(MARIO_ATLAS)).apply {
        addAnimation(GROWING, 0.15f, *enumerateString("mario-grow-", 1, 10))
    }

    override val stateId: MarioStateId = GROWING_MARIO

    override val body: Body = defineBody(initialPosition)
    override val fixtures: FixturesMap<MarioFixtures> = defineFixtures()

    override val frame: TextureRegion
        get() = textures.getCurrentTexture(state)

    override fun update(delta: Float) {
        state.update(delta)
        body.setTransform(body.position.x, (initialPosition.y + 8f.toMeters() - BIG_MARIO_SIZE.height / 2 + (frame.regionHeight.toMeters() - 5f.toMeters()) / 2) , 0f)
        // TODO Should transform texture instead of body

        if (textures.isAnimationFinished(state)) {
            mario.changeState { MarioBigState(mario, body.position) }
        }
    }

    override fun moveLeft() {
        // Growing mario can't move
    }

    override fun moveRight() {
        // Growing mario can't move
    }

    override fun jump() {
        // Growing mario can't jump
    }

    private fun defineBody(pos: Vector2): Body {
        return gameScreen.world.body(type = StaticBody) {
            position.set(pos.x, pos.y + 8f.toMeters())
            userData = mario
        }
    }

    private fun defineFixtures(): FixturesMap<MarioFixtures> {
        return mutableMapOf(
            BODY to body.box(BIG_MARIO_SIZE.width, BIG_MARIO_SIZE.height) {
                filter.categoryBits = MARIO_BIT
                filter.maskBits = MARIO_COLLISION_MASK
                userData = BODY
            }
        )
    }

}