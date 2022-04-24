package com.matthias.mario.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.*
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.assets.AssetManager
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
import com.matthias.mario.V_HEIGHT
import com.matthias.mario.V_WIDTH
import com.matthias.mario.common.centerX
import com.matthias.mario.common.toMeters
import com.matthias.mario.listeners.WorldContactListener
import com.matthias.mario.scenes.Hud
import com.matthias.mario.sprites.Mario
import com.matthias.mario.utils.B2DWorldCreator.createWorld
import ktx.box2d.earthGravity

class GameScreen(val game: MarioGame) : ScreenAdapter() {

    val assetManager = AssetManager().apply {
        load("mario.atlas", com.badlogic.gdx.graphics.g2d.TextureAtlas::class.java)
        finishLoading()
    }

    val camera = OrthographicCamera()
    val viewport = FitViewport(V_WIDTH.toMeters(), V_HEIGHT.toMeters(), camera)
    val hud = Hud(game.batch)

    val map = TmxMapLoader().load("levels/level_1-1.tmx")
    val mapRenderer = OrthogonalTiledMapRenderer(map, 1.toMeters())
    val b2ddr = Box2DDebugRenderer()

    val world = World(earthGravity, true).apply { setContactListener(WorldContactListener()) }
    val mario = Mario(this)

    init {
        camera.position.y = viewport.worldHeight / 2f
        createWorld(this)
    }

    override fun render(delta: Float) {
        update(delta)

        clear(BLACK)
        mapRenderer.render()

        b2ddr.render(world, camera.combined)

        game.batch.projectionMatrix = camera.combined
        game.batch.begin()
        mario.draw(game.batch)
        game.batch.end()

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
        assetManager.dispose()
    }

    private fun update(delta: Float) {
        handleInput(delta)

        world.step(1 / 60f, 6, 2)

        mario.update(delta)

        camera.position.x = mario.centerX
        camera.update()
        mapRenderer.setView(camera)
    }

    private fun handleInput(delta: Float) {
        if (Gdx.input.isKeyJustPressed(UP)) {
            mario.body.applyLinearImpulse(Vector2(0f, 4f), mario.body.worldCenter, true)
        }

        if (Gdx.input.isKeyPressed(LEFT) && mario.body.linearVelocity.x >= -2f) {
            mario.body.applyLinearImpulse(Vector2(-0.1f, 0f), mario.body.worldCenter, true)
        }

        if (Gdx.input.isKeyPressed(RIGHT) && mario.body.linearVelocity.x <= 2f) {
            mario.body.applyLinearImpulse(Vector2(0.1f, 0f), mario.body.worldCenter, true)
        }
    }
}