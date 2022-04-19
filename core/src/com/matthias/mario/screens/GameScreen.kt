package com.matthias.mario.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.*
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
import com.matthias.mario.MarioGame.Companion.PPM
import com.matthias.mario.MarioGame.Companion.V_HEIGHT
import com.matthias.mario.MarioGame.Companion.V_WIDTH
import com.matthias.mario.common.centerX
import com.matthias.mario.common.centerY
import com.matthias.mario.common.halfHeight
import com.matthias.mario.common.halfWidth
import com.matthias.mario.scenes.Hud
import com.matthias.mario.sprites.Mario

class GameScreen(private val game: MarioGame) : ScreenAdapter() {

    private val camera = OrthographicCamera()
    private val viewport = FitViewport(V_WIDTH / PPM, V_HEIGHT / PPM, camera)
    private val hud = Hud(game.batch)

    private val map = TmxMapLoader().load("levels/level_1-1.tmx")
    private val mapRenderer = OrthogonalTiledMapRenderer(map, 1 / PPM)

    private val world = World(Vector2(0f, -10f), true)
    private val b2ddr = Box2DDebugRenderer()

    private val mario = Mario(world)

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

    override fun dispose() {
        map.dispose()
        mapRenderer.dispose()
        world.dispose()
        b2ddr.dispose()
        hud.dispose()
    }

    private fun update(delta: Float) {
        handleInput(delta)
        world.step(1/60f, 6, 2)

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

    private fun createBodiesForLayer(layerName: String) {
        val bodyDef = BodyDef()
        val polygonShape = PolygonShape()
        val fixtureDef = FixtureDef().apply { shape = polygonShape }

        for (obj in map.layers.get(layerName).objects.getByType(RectangleMapObject::class.java)) {
            bodyDef.position.set(obj.rectangle.centerX / PPM, obj.rectangle.centerY / PPM)
            polygonShape.setAsBox(obj.rectangle.halfWidth / PPM, obj.rectangle.halfHeight / PPM)
            world.createBody(bodyDef).createFixture(fixtureDef)
        }
    }
}