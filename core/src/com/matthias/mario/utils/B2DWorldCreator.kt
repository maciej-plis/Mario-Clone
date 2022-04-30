package com.matthias.mario.utils

import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.physics.box2d.Body
import com.matthias.mario.extensions.*
import com.matthias.mario.screens.GameScreen
import com.matthias.mario.sprites.Brick
import com.matthias.mario.sprites.Goomba
import com.matthias.mario.sprites.Koopa
import com.matthias.mario.sprites.MysteryBlock
import ktx.box2d.body
import ktx.box2d.polygon

object B2DWorldCreator {

    const val BACKGROUND_LAYER = "Background"
    const val GRAPHICS_LAYER = "Graphics"
    const val GROUND_LAYER = "Ground"
    const val PIPES_LAYER = "Pipes"
    const val BRICKS_LAYER = "Bricks"
    const val MYSTERY_BLOCKS_LAYER = "Mystery-Blocks"
    const val GOOMBAS_LAYER = "Goombas"
    const val KOOPAS_LAYER = "Koopas"

    fun createWorld(gameScreen: GameScreen) {
        createGroundLayer(gameScreen)
        createPipesLayer(gameScreen)
        createBricksLayer(gameScreen)
        createMysteryBlockLayer(gameScreen)
        createGoombas(gameScreen)
        createKoopas(gameScreen)
    }

    fun createTileBody(gameScreen: GameScreen, obj: RectangleMapObject, tile: Any? = null): Body {
        return gameScreen.world.body {
            position.set(obj.rectangle.centerX.toMeters(), obj.rectangle.centerY.toMeters())
            polygon {
                it.setAsBox(obj.rectangle.halfWidth.toMeters(), obj.rectangle.halfHeight.toMeters())
                userData = tile
            }
        }
    }

    private fun createGroundLayer(gameScreen: GameScreen) {
        for (obj in gameScreen.map.layers.get(GROUND_LAYER).rectangleObjects) {
            createTileBody(gameScreen, obj)
        }
    }

    private fun createPipesLayer(gameScreen: GameScreen) {
        for (obj in gameScreen.map.layers.get(PIPES_LAYER).rectangleObjects) {
            createTileBody(gameScreen, obj)
        }
    }

    private fun createBricksLayer(gameScreen: GameScreen) {
        for (obj in gameScreen.map.layers.get(BRICKS_LAYER).rectangleObjects) {
            Brick(gameScreen, obj)
        }
    }

    private fun createMysteryBlockLayer(gameScreen: GameScreen) {
        for (obj in gameScreen.map.layers.get(MYSTERY_BLOCKS_LAYER).rectangleObjects) {
            MysteryBlock(gameScreen, obj)
        }
    }

    private fun createGoombas(gameScreen: GameScreen) {
        for (obj in gameScreen.map.layers.get(GOOMBAS_LAYER).rectangleObjects) {
            Goomba(gameScreen, obj.rectangle.center)
        }
    }

    private fun createKoopas(gameScreen: GameScreen) {
        for (obj in gameScreen.map.layers.get(KOOPAS_LAYER).rectangleObjects) {
            Koopa(gameScreen, obj.rectangle.center)
        }
    }
}