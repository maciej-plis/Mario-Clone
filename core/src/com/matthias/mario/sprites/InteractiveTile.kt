package com.matthias.mario.sprites

import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Fixture
import com.matthias.mario.common.*
import com.matthias.mario.screens.GameScreen
import com.matthias.mario.utils.B2DWorldCreator
import ktx.box2d.body
import ktx.box2d.polygon

abstract class InteractiveTile(protected val gameScreen: GameScreen, obj: RectangleMapObject) {

    protected val body: Body = gameScreen.world.body { position.set(obj.rectangle.centerX.toMeters(), obj.rectangle.centerY.toMeters()) }
    protected val fixture: Fixture = body.polygon { it.setAsBox(obj.rectangle.halfWidth.toMeters(), obj.rectangle.halfHeight.toMeters()) }

    abstract fun onHeadCollision()

    val cell: Cell?
        get() {
            val layer = gameScreen.map.layers.get(B2DWorldCreator.GRAPHICS_LAYER) as TiledMapTileLayer
            return layer.getCell(body.position.x.toPixels().toInt() / layer.tileWidth, body.position.y.toPixels().toInt() / layer.tileHeight)
        }
}