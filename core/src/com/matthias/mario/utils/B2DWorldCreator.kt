package com.matthias.mario.utils

import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.matthias.mario.MarioGame.Companion.PPM
import com.matthias.mario.common.*
import com.matthias.mario.screens.GameScreen
import com.matthias.mario.sprites.Brick
import com.matthias.mario.sprites.MysteryBlock

object B2DWorldCreator {

    private const val GROUND_LAYER = "Ground"
    private const val PIPES_LAYER = "Pipes"
    private const val BRICKS_LAYER = "Bricks"
    private const val MYSTERY_BLOCKS_LAYER = "Mystery-Blocks"

    val BODY_DEF = BodyDef()
    val SHAPE = PolygonShape()
    val FIXTURE_DEF = FixtureDef().apply { shape = SHAPE }

    fun createWorld(gameScreen: GameScreen) {
        createGroundLayer(gameScreen)
        createPipesLayer(gameScreen)
        createBricksLayer(gameScreen)
        createMysteryBlockLayer(gameScreen)
    }

    private fun createGroundLayer(gameScreen: GameScreen) {
        for (obj in gameScreen.map.layers.get(GROUND_LAYER).rectangleObjects) {
            BODY_DEF.position.set(obj.rectangle.centerX / PPM, obj.rectangle.centerY / PPM)
            SHAPE.setAsBox(obj.rectangle.halfWidth / PPM, obj.rectangle.halfHeight / PPM)
            gameScreen.world.createBodyWithFixtures(BODY_DEF, FIXTURE_DEF)
        }
    }

    private fun createPipesLayer(gameScreen: GameScreen) {
        for (obj in gameScreen.map.layers.get(PIPES_LAYER).rectangleObjects) {
            BODY_DEF.position.set(obj.rectangle.centerX / PPM, obj.rectangle.centerY / PPM)
            SHAPE.setAsBox(obj.rectangle.halfWidth / PPM, obj.rectangle.halfHeight / PPM)
            gameScreen.world.createBodyWithFixtures(BODY_DEF, FIXTURE_DEF)
        }
    }

    private fun createBricksLayer(gameScreen: GameScreen) {
        for (obj in gameScreen.map.layers.get(BRICKS_LAYER).rectangleObjects) {
            Brick(gameScreen, obj.rectangle)
        }
    }

    private fun createMysteryBlockLayer(gameScreen: GameScreen) {
        for (obj in gameScreen.map.layers.get(MYSTERY_BLOCKS_LAYER).rectangleObjects) {
            MysteryBlock(gameScreen, obj.rectangle)
        }
    }
}