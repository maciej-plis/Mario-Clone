package com.matthias.mario.common

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Array

fun World.createBodyWithFixtures(bodyDef: BodyDef, vararg fixtureDefs: FixtureDef): Body {
    return createBody(bodyDef).apply {
        for (fixtureDef in fixtureDefs) {
            createFixture(fixtureDef)
        }
    }
}

val MapLayer.rectangleObjects: Array<RectangleMapObject>
    get() = this.objects.getByType(RectangleMapObject::class.java)


fun AssetManager.getAtlas(fileName: String): TextureAtlas {
    return this.get(fileName, TextureAtlas::class.java)
}

inline fun <reified T> Sequence<T>.toArray(): Array<T> {
    val array = Array<T>()
    for (item in this) {
        array.add(item)
    }
    return array
}

fun TextureAtlas.findRegions(vararg names: String): Array<TextureRegion> {
    return names.asSequence()
        .map { findRegion(it) }
        .toArray()
}