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
import com.matthias.mario.sprites.mario.Mario.Companion.MARIO_COLLISION_MASK
import com.matthias.mario.sprites.mario.Mario.Companion.MarioFixtures
import com.matthias.mario.sprites.mario.Mario.Companion.MarioFixtures.BODY
import com.matthias.mario.sprites.mario.Mario.Companion.SMALL_MARIO_SIZE
import com.matthias.mario.sprites.mario.Mario.Companion.State
import com.matthias.mario.sprites.mario.Mario.Companion.State.GROWING
import com.matthias.mario.sprites.mario.MarioState.Companion.MarioStateId
import com.matthias.mario.sprites.mario.MarioState.Companion.MarioStateId.SHRINKING_MARIO
import ktx.box2d.body
import ktx.box2d.box

class MarioShrinkingState(private val mario: Mario, private val initialPosition: Vector2) : MarioState {

    private val gameScreen: GameScreen = mario.gameScreen

    private val state = EntityState(GROWING)
    private val textures = EntityTextures<State>(gameScreen.assetManager.getAtlas(MARIO_ATLAS)).apply {
        addAnimation(GROWING, 0.75f, *enumerateString("mario-shrink-", 1, 2))
    }

    override val stateId: MarioStateId = SHRINKING_MARIO

    override val body: Body = defineBody(initialPosition)
    override val fixtures: FixturesMap<MarioFixtures> = defineFixtures()

    override val frame: TextureRegion
        get() = textures.getCurrentTexture(state)

    override fun update(delta: Float) {
        state.update(delta)
        body.setTransform(body.position.x, (initialPosition.y - 8f.toMeters() - SMALL_MARIO_SIZE.height / 2 + (frame.regionHeight.toMeters() + 5f.toMeters()) / 2) , 0f)
        // TODO Should transform texture instead of body

        if (textures.isAnimationFinished(state)) {
            mario.changeState { MarioSmallState(mario, body.position) }
        }
    }

    override fun moveLeft() {
        // Shrinking mario can't move
    }

    override fun moveRight() {
        // Shrinking mario can't move
    }

    override fun jump() {
        // Shrinking mario can't jump
    }

    private fun defineBody(pos: Vector2): Body {
        return gameScreen.world.body(type = StaticBody) {
            position.set(pos.x, pos.y - 8f.toMeters())
            userData = mario
        }
    }

    private fun defineFixtures(): FixturesMap<MarioFixtures> {
        return mutableMapOf(
            BODY to body.box(SMALL_MARIO_SIZE.width, SMALL_MARIO_SIZE.height) {
                filter.categoryBits = MARIO_BIT
                filter.maskBits = MARIO_COLLISION_MASK
                userData = BODY
            }
        )
    }

}