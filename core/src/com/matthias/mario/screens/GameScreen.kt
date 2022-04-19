package com.matthias.mario.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.*
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color.BLACK
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.ScreenUtils.clear
import com.badlogic.gdx.utils.viewport.FitViewport
import com.matthias.mario.MarioGame
import com.matthias.mario.MarioGame.Companion.PPM
import com.matthias.mario.MarioGame.Companion.V_HEIGHT
import com.matthias.mario.MarioGame.Companion.V_WIDTH
import com.matthias.mario.scenes.Hud
import com.matthias.mario.sprites.Mario
import com.matthias.mario.utils.B2DWorldCreator.createWorld
import kotlin.math.roundToInt

class GameScreen(private val game: MarioGame) : ScreenAdapter() {

    private val camera = OrthographicCamera()
    private val viewport = FitViewport(V_WIDTH / PPM, V_HEIGHT / PPM, camera)
    private val hud = Hud(game.batch)

    val map = TmxMapLoader().load("levels/level_1-1.tmx")
    private val mapRenderer = OrthogonalTiledMapRenderer(map, 1 / PPM)

    val world = World(Vector2(0f, -10f), true)
    private val b2ddr = Box2DDebugRenderer()

    private val mario = Mario(world)

    init {
        camera.position.set(viewport.worldWidth / 2f, viewport.worldHeight / 2f, 0f)
        createWorld(this)
    }

    override fun render(delta: Float) {
        update(delta)

        clear(BLACK)
        mapRenderer.render()
        b2ddr.render(world, camera.combined)
        game.batch.projectionMatrix = hud.stage.camera.combined
        hud.stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun dispose() {
        map.dispose()
        mapRenderer.dispose()
        world.dispose()
        b2ddr.dispose()
        hud.dispose()
    }

    private fun update(delta: Float) {
        handleInput(delta)

        world.step(1 / 60f, 6, 2)

        camera.position.x = mario.body.position.x
        camera.update()
        mapRenderer.setView(camera)
    }

    private fun handleInput(delta: Float) {
        if (Gdx.input.isKeyJustPressed(UP)) {
            mario.body.applyLinearImpulse(Vector2(0f, 4f), mario.body.worldCenter, true)
        }

        if (Gdx.input.isKeyPressed(LEFT) && mario.body.linearVelocity.x <= 2f) {
            mario.body.applyLinearImpulse(Vector2(-0.1f, 0f), mario.body.worldCenter, true)
        }

        if (Gdx.input.isKeyPressed(RIGHT) && mario.body.linearVelocity.x <= 2f) {
            mario.body.applyLinearImpulse(Vector2(0.1f, 0f), mario.body.worldCenter, true)
        }
    }
}