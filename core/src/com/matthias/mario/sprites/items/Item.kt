package com.matthias.mario.sprites.items

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.matthias.mario.common.FixturesMap
import com.matthias.mario.common.ITEMS_ATLAS
import com.matthias.mario.extensions.XDirection
import com.matthias.mario.extensions.XDirection.RIGHT
import com.matthias.mario.extensions.getAtlas
import com.matthias.mario.screens.GameScreen

abstract class Item(protected val gameScreen: GameScreen) : Sprite() {

    protected val itemsAtlas: TextureAtlas = gameScreen.assetManager.getAtlas(ITEMS_ATLAS)

    abstract var body: Body
    abstract var fixtures: FixturesMap

    val velocity = Vector2(0f, 0f)

    var stateTimer: Float = 0f
    var direction: XDirection = RIGHT

    abstract fun update()

    abstract fun use()

    fun turnAround() {
        direction = if (direction == XDirection.LEFT) RIGHT else XDirection.LEFT
        velocity.scl(-1f, 0f)
    }
}