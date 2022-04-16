package com.matthias.mario

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.Color.BLACK
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils

class MarioGame : Game() {

    lateinit var batch: SpriteBatch

    override fun create() {
        batch = SpriteBatch()
    }

    override fun render() {
        ScreenUtils.clear(BLACK)
    }

    override fun dispose() {
        batch.dispose()
    }
}