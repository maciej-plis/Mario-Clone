package com.matthias.mario.sprites.enemies

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Fixture
import com.matthias.mario.common.ENEMIES_ATLAS
import com.matthias.mario.extensions.XDirection
import com.matthias.mario.extensions.XDirection.LEFT
import com.matthias.mario.extensions.XDirection.RIGHT
import com.matthias.mario.extensions.getAtlas
import com.matthias.mario.screens.GameScreen

abstract class Enemy(protected val gameScreen: GameScreen) : Sprite() {

    protected val enemiesAtlas: TextureAtlas = gameScreen.assetManager.getAtlas(ENEMIES_ATLAS)

    abstract var body: Body
    abstract var fixtures: MutableMap<String, Fixture>

    open var velocity = Vector2(0f, 0f)

    var stateTimer: Float = 0f
    var direction: XDirection = LEFT

    abstract fun update(delta: Float)

    abstract fun onHeadHit()

    /**
     * Change movement direction on x-axis
     * i.e. Change direction and reverse velocity
     */
    fun turnAround() {
        direction = if (direction == LEFT) RIGHT else LEFT
        velocity.scl(-1f, 0f)
    }
}