package com.matthias.mario.extensions

import com.badlogic.gdx.math.Rectangle

val Rectangle.halfWidth: Float
    get() = this.width / 2f

val Rectangle.halfHeight: Float
    get() = this.height / 2

val Rectangle.centerX: Float
    get() = this.x + this.halfWidth

val Rectangle.centerY: Float
    get() = this.y + this.halfHeight