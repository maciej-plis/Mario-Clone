package com.matthias.mario.sprites

import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.World
import com.matthias.mario.utils.B2DWorldCreator.createTileBody

abstract class InteractiveTile(world: World, obj: RectangleMapObject) {

    private val body: Body = createTileBody(world, obj)
}