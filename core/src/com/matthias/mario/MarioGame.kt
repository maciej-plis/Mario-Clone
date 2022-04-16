package com.matthias.mario

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.matthias.mario.screens.GameScreen

class MarioGame : Game() {

    companion object {
        const val V_WIDTH  = 800f
        const val V_HEIGHT = 600f
    }

    lateinit var batch: SpriteBatch

    override fun create() {
        batch = SpriteBatch()
        setScreen(GameScreen(this))
    }

    override fun dispose() {
        batch.dispose()
    }
}