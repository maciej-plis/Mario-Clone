package com.matthias.mario.screens

import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color.BLACK
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.ScreenUtils.clear
import com.badlogic.gdx.utils.viewport.FitViewport
import com.matthias.mario.MarioGame
import com.matthias.mario.MarioGame.Companion.V_HEIGHT
import com.matthias.mario.MarioGame.Companion.V_WIDTH
import com.matthias.mario.scenes.Hud

class GameScreen(private val game: MarioGame) : ScreenAdapter() {

    private val camera = OrthographicCamera()
    private val viewport = FitViewport(V_WIDTH, V_HEIGHT, camera)
    private val hud = Hud(game.batch)


    override fun render(delta: Float) {
        clear(BLACK)

        game.batch.projectionMatrix = hud.stage.camera.combined
        hud.stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }
}