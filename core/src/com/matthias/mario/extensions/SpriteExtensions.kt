package com.matthias.mario.extensions

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2

val Sprite.halfWidth: Float
    get() = this.width / 2f

val Sprite.halfHeight: Float
    get() = this.height / 2

val Sprite.halfSize: Vector2
    get() = Vector2(halfWidth, halfHeight)

val Sprite.topLeft: Vector2
    get() = Vector2(x, y + height)

val Sprite.topRight: Vector2
    get() = Vector2(x + width, y + height)

val Sprite.bottomRight: Vector2
    get() = Vector2(x + width, y)

val Sprite.bottomLeft: Vector2
    get() = Vector2(x, y)

val Sprite.center: Vector2
    get() = Vector2(x + halfWidth, y + halfWidth)

val Sprite.top: Float
    get() = y + height

val Sprite.bottom: Float
    get() = y

val Sprite.left: Float
    get() = x

val Sprite.right: Float
    get() = x + width

val Sprite.centerX: Float
    get() = x + halfWidth

val Sprite.centerY: Float
    get() = y + halfHeight

fun Sprite.setCenter(center: Vector2) {
    setCenter(center.x, center.y)
}

fun Sprite.setOriginBasedPosition(position: Vector2) {
    setOriginBasedPosition(position.x, position.y)
}

fun Sprite.setOrigin(origin: Vector2) {
    setOrigin(origin.x, origin.y)
}
