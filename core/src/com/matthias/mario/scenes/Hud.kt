package com.matthias.mario.scenes

import com.badlogic.gdx.graphics.Color.WHITE
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.FitViewport
import com.matthias.mario.MarioGame.Companion.V_HEIGHT
import com.matthias.mario.MarioGame.Companion.V_WIDTH

class Hud(spriteBatch: SpriteBatch) {

    val stage: Stage

    private var worldTimer = 300
    private var timeCount = 0f
    private var score = 0

    private val countdownLabel: Label
    private val scoreLabel: Label
    private val timeLabel: Label
    private val levelLabel: Label
    private val worldLabel: Label
    private val marioLabel: Label

    init {
        val viewport = FitViewport(V_WIDTH, V_HEIGHT, OrthographicCamera())
        stage = Stage(viewport, spriteBatch)

        val labelStyle = LabelStyle(BitmapFont(), WHITE)
        countdownLabel = Label(String.format("%03d", worldTimer), labelStyle)
        scoreLabel = Label(String.format("%06d", score), labelStyle)
        timeLabel = Label("TIME", labelStyle)
        levelLabel = Label("1-1", labelStyle)
        worldLabel = Label("WORLD", labelStyle)
        marioLabel = Label("MARIO", labelStyle)

        val table = Table().apply {
            top()
            setFillParent(true)
            add(marioLabel).expandX().padTop(10f)
            add(worldLabel).expandX().padTop(10f)
            add(timeLabel).expandX().padTop(10f)
            row()
            add(scoreLabel).expandX()
            add(levelLabel).expandX()
            add(countdownLabel).expandX()
        }
        stage.addActor(table)
    }
}