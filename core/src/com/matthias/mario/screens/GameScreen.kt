package com.matthias.mario.screens

import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color.BLACK
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.ScreenUtils.clear
import com.badlogic.gdx.utils.viewport.FitViewport
import com.matthias.mario.MarioGame
import com.matthias.mario.V_HEIGHT
import com.matthias.mario.V_WIDTH
import com.matthias.mario.common.*
import com.matthias.mario.extensions.centerX
import com.matthias.mario.extensions.toMeters
import com.matthias.mario.listeners.WorldContactListener
import com.matthias.mario.scenes.Hud
import com.matthias.mario.sprites.enemies.Enemy
import com.matthias.mario.sprites.items.Item
import com.matthias.mario.sprites.mario.Mario
import com.matthias.mario.utils.B2DWorldCreator.createWorld
import ktx.box2d.earthGravity

class GameScreen(val game: MarioGame) : ScreenAdapter() {

    val assetManager = AssetManager().apply {
        load(MARIO_ATLAS, TextureAtlas::class.java)
        load(ENEMIES_ATLAS, TextureAtlas::class.java)
        load(ITEMS_ATLAS, TextureAtlas::class.java)
        load(OVERWORLD_MUSIC, Music::class.java)
        load(COIN_SOUND, Sound::class.java)
        load(BUMP_SOUND, Sound::class.java)
        load(JUMP_SMALL_SOUND, Sound::class.java)
        load(JUMP_BIG_SOUND, Sound::class.java)
        load(BRAKE_BLOCK_SOUND, Sound::class.java)
        load(POWER_UP_APPEARS_SOUND, Sound::class.java)
        load(MARIO_GROW_SOUND, Sound::class.java)
        load(PIPE_SOUND, Sound::class.java)
        load(STOMP_SOUND, Sound::class.java)
        finishLoading()
    }

    val camera = OrthographicCamera()
    val viewport = FitViewport(V_WIDTH.toMeters(), V_HEIGHT.toMeters(), camera)
    val hud = Hud(game.batch)

    val map = TmxMapLoader().load(LEVEL_1_1_MAP)
    val mapRenderer = OrthogonalTiledMapRenderer(map, 1.toMeters())
    val b2ddr = Box2DDebugRenderer()

    val world = World(earthGravity, true).apply { setContactListener(WorldContactListener()) }

    val mario = Mario(this)
    val enemies: MutableList<Enemy> = mutableListOf()
    val items: MutableList<Item> = mutableListOf()

//    val music = assetManager.getMusic(OVERWORLD_MUSIC, isLooping = true).play()

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
        enemies.forEach { it.draw(game.batch) }
        items.forEach { it.draw(game.batch) }
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
        mario.handleInput()

        world.step(1 / 60f, 8, 3)

        mario.update(delta)

        enemies.forEach {enemy ->
            if(enemy.x < mario.x + 224.toMeters() ) {
                enemy.body.isActive = true
            }
            enemy.update(delta)
        }
        items.forEach { it.update() }

        camera.position.x = mario.centerX
        camera.update()
        mapRenderer.setView(camera)
    }
}