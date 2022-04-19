package com.matthias.mario.common

import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Array

fun World.createBodyWithFixture(bodyDef: BodyDef, fixtureDef: FixtureDef): Body {
    return createBody(bodyDef).apply { createFixture(fixtureDef) }
}

val MapLayer.rectangleObjects: Array<RectangleMapObject>
    get() = this.objects.getByType(RectangleMapObject::class.java)