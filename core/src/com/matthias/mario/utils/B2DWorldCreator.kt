package com.matthias.mario.utils

import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.World
import com.matthias.mario.MarioGame.Companion.PPM
import com.matthias.mario.common.*
import com.matthias.mario.sprites.Brick
import com.matthias.mario.sprites.MysteryBlock
import ktx.box2d.body
import ktx.box2d.polygon

object B2DWorldCreator {

    private const val GROUND_LAYER = "Ground"
    private const val PIPES_LAYER = "Pipes"
    private const val BRICKS_LAYER = "Bricks"
    private const val MYSTERY_BLOCKS_LAYER = "Mystery-Blocks"

    fun createWorld(world: World, map: TiledMap) {
        createGroundLayer(world, map)
        createPipesLayer(world, map)
        createBricksLayer(world, map)
        createMysteryBlockLayer(world, map)
    }

    fun createTileBody(world: World, obj: RectangleMapObject): Body {
        return world.body {
            position.set(obj.rectangle.centerX / PPM, obj.rectangle.centerY / PPM)
            polygon {
                it.setAsBox(obj.rectangle.halfWidth / PPM, obj.rectangle.halfHeight / PPM)
            }
        }
    }

    private fun createGroundLayer(world: World, map: TiledMap) {
        for (obj in map.layers.get(GROUND_LAYER).rectangleObjects) {
            createTileBody(world, obj)
        }
    }

    private fun createPipesLayer(world: World, map: TiledMap) {
        for (obj in map.layers.get(PIPES_LAYER).rectangleObjects) {
            createTileBody(world, obj)
        }
    }

    private fun createBricksLayer(world: World, map: TiledMap) {
        for (obj in map.layers.get(BRICKS_LAYER).rectangleObjects) {
            Brick(world, obj)
        }
    }

    private fun createMysteryBlockLayer(world: World, map: TiledMap) {
        for (obj in map.layers.get(MYSTERY_BLOCKS_LAYER).rectangleObjects) {
            MysteryBlock(world, obj)
        }
    }
}