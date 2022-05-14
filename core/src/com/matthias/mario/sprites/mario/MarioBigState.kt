package com.matthias.mario.sprites.mario

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.matthias.mario.common.*
import com.matthias.mario.extensions.*
import com.matthias.mario.screens.GameScreen
import com.matthias.mario.sprites.mario.Mario.Companion.BIG_MARIO_SIZE
import com.matthias.mario.sprites.mario.Mario.Companion.MARIO_COLLISION_MASK
import com.matthias.mario.sprites.mario.Mario.Companion.MarioFixtures
import com.matthias.mario.sprites.mario.Mario.Companion.MarioFixtures.*
import com.matthias.mario.sprites.mario.Mario.Companion.State
import com.matthias.mario.sprites.mario.Mario.Companion.State.*
import com.matthias.mario.sprites.mario.MarioState.Companion.MarioStateId
import com.matthias.mario.sprites.mario.MarioState.Companion.MarioStateId.BIG_MARIO
import ktx.box2d.body
import ktx.box2d.box
import ktx.box2d.edge

class MarioBigState(private val mario: Mario, position: Vector2) : MarioState {

    companion object {
        val MOVE_LEFT_VECTOR = Vector2(-0.1f, 0f)
        val MOVE_RIGHT_VECTOR = Vector2(0.1f, 0f)
        val JUMP_VECTOR = Vector2(0f, 4f)
    }

    private val gameScreen: GameScreen = mario.gameScreen

    private val state = EntityState(STANDING)
    private val textures = EntityTextures<State>(gameScreen.assetManager.getAtlas(MARIO_ATLAS)).apply {
        addTexture(STANDING, "big-mario-stand")
        addTexture(JUMPING, "big-mario-jump")
        addAnimation(RUNNING, 0.1f, *enumerateString("big-mario-walk-", 1, 3), mode = LOOP)
        addAnimation(SHRINKING, 0.1f, *enumerateString("mario-shrink-", 1, 2))
    }

    override val stateId: MarioStateId = BIG_MARIO

    override val body: Body = defineBody(position)
    override val fixtures: FixturesMap<MarioFixtures> = defineFixtures()

    override val frame: TextureRegion
        get() = textures.getCurrentTexture(state)

    override fun update(delta: Float) {
        updateState(delta)
    }

    override fun moveLeft() {
        body.applyLinearImpulse(MOVE_LEFT_VECTOR, body.worldCenter, true)
    }

    override fun moveRight() {
        body.applyLinearImpulse(MOVE_RIGHT_VECTOR, body.worldCenter, true)
    }

    override fun jump() {
        body.applyLinearImpulse(JUMP_VECTOR, body.worldCenter, true)
        gameScreen.assetManager.getSound(JUMP_BIG_SOUND).play()
    }

    private fun updateState(delta: Float) {
        val currentState = when {
            body.linearVelocity.y != 0f -> JUMPING
            body.linearVelocity.x != 0f -> RUNNING
            else -> STANDING
        }
        state.update(currentState, delta)
    }

    private fun defineBody(pos: Vector2): Body {
        return gameScreen.world.body(type = DynamicBody) {
            position.set(pos)
            userData = mario
        }
    }

    private fun defineFixtures(): FixturesMap<MarioFixtures> {
        return mutableMapOf(
            BODY to body.box(BIG_MARIO_SIZE.width, BIG_MARIO_SIZE.height) {
                filter.categoryBits = MARIO_BIT
                filter.maskBits = MARIO_COLLISION_MASK
                userData = BODY
            },
            FEET to body.edge(Vector2(-2f, -14f).sclToMeters(), Vector2(2f, -14f).sclToMeters()) {
                filter.categoryBits = MARIO_BIT
                filter.maskBits = MARIO_COLLISION_MASK
                userData = FEET
            },
            HEAD to body.edge(Vector2(-2f, 14f).sclToMeters(), Vector2(2f, 14f).sclToMeters()) {
                filter.categoryBits = MARIO_BIT
                filter.maskBits = MARIO_COLLISION_MASK
                userData = HEAD
                isSensor = true
            }
        )
    }
}