package com.matthias.mario.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.LEFT
import com.badlogic.gdx.Input.Keys.RIGHT
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color.BLACK
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.utils.ScreenUtils.clear
import com.badlogic.gdx.utils.viewport.FitViewport
import com.matthias.mario.MarioGame
import com.matthias.mario.MarioGame.Companion.V_HEIGHT
import com.matthias.mario.MarioGame.Companion.V_WIDTH
import com.matthias.mario.common.centerX
import com.matthias.mario.common.centerY
import com.matthias.mario.common.halfHeight
import com.matthias.mario.common.halfWidth
import com.matthias.mario.scenes.Hud

class GameScreen(private val game: MarioGame) : ScreenAdapter() {

    private val camera = OrthographicCamera()
    private val viewport = FitViewport(V_WIDTH, V_HEIGHT, camera)
    private val hud = Hud(game.batch)

    private val mapLoader = TmxMapLoader()
    private val map = mapLoader.load("levels/level_1-1.tmx")
    private val mapRenderer = OrthogonalTiledMapRenderer(map)

    private val world = World(Vector2(0f, 0f), true)
    private val b2ddr = Box2DDebugRenderer()

    init {
        camera.position.set(viewport.worldWidth / 2f, viewport.worldHeight / 2f, 0f)
        createBodiesForLayer("Ground")
        createBodiesForLayer("Pipes")
        createBodiesForLayer("Bricks")
        createBodiesForLayer("Mystery-Blocks")
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

    private fun update(delta: Float) {
        handleInput(delta)
        camera.update()
        mapRenderer.setView(camera)
    }

    private fun handleInput(delta: Float) {
        if (Gdx.input.isKeyPressed(LEFT)) {
            camera.position.x -= 250 * delta
        }

        if (Gdx.input.isKeyPressed(RIGHT)) {
            camera.position.x += 250 * delta
        }
    }

    private fun createBodiesForLayer(layerName: String) {
        val bodyDef = BodyDef()
        val polygonShape = PolygonShape()
        val fixtureDef = FixtureDef().apply { shape = polygonShape }

        for (obj in map.layers.get(layerName).objects.getByType(RectangleMapObject::class.java)) {
            bodyDef.position.set(obj.rectangle.centerX, obj.rectangle.centerY)
            polygonShape.setAsBox(obj.rectangle.halfWidth, obj.rectangle.halfHeight)
            world.createBody(bodyDef).createFixture(fixtureDef)
        }
    }
}