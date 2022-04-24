package com.matthias.mario.sprites

import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.World
import com.matthias.mario.common.*
import ktx.box2d.body
import ktx.box2d.polygon

abstract class InteractiveTile(world: World, obj: RectangleMapObject) {

    protected val body: Body = world.body { position.set(obj.rectangle.centerX.toMeters(), obj.rectangle.centerY.toMeters()) }
    protected val fixture: Fixture = body.polygon { it.setAsBox(obj.rectangle.halfWidth.toMeters(), obj.rectangle.halfHeight.toMeters()) }

    abstract fun onHeadCollision()
}