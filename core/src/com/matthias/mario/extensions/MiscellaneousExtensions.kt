package com.matthias.mario.extensions

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.MapProperties
import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.matthias.mario.PPM
import com.matthias.mario.sprites.tiles.TileContent

val MapLayer.rectangleObjects: Array<RectangleMapObject>
    get() = this.objects.getByType(RectangleMapObject::class.java)

val MapLayer.polygonObjects: Array<PolygonMapObject>
    get() = this.objects.getByType(PolygonMapObject::class.java)

fun AssetManager.getAtlas(fileName: String): TextureAtlas {
    return this.get(fileName, TextureAtlas::class.java)
}

fun AssetManager.getMusic(fileName: String, isLooping: Boolean = false): Music {
    return this.get(fileName, Music::class.java).apply {
        this.isLooping = isLooping
    }
}

fun AssetManager.getSound(fileName: String): Sound {
    return this.get(fileName, Sound::class.java)
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

fun Float.toMeters(): Float {
    return this / PPM
}

fun Int.toMeters(): Float {
    return this / PPM
}

fun Float.toPixels(): Float {
    return this * PPM
}

fun Int.toPixels(): Float {
    return this * PPM
}

fun Vector2.sclToMeters(): Vector2 {
    return scl(1f.toMeters())
}

fun MapProperties.getContent(key: String, default: TileContent): TileContent {
    val value = get(key)
    if(value is String) {
        return TileContent.valueOfOrDefault(value, default)
    }
    return default
}