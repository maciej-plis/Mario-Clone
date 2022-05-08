package com.matthias.mario.extensions

import com.badlogic.gdx.graphics.g2d.TextureRegion

fun TextureRegion.flipX() {
    flip(true, false)
}

fun TextureRegion.flipY() {
    flip(false, true)
}

fun TextureRegion.inDirection(xDirection: XDirection): TextureRegion {
    val isFacingLeftButNotFlipped = xDirection == XDirection.LEFT && !isFlipX
    val isFacingRightButFlipped = xDirection == XDirection.RIGHT && isFlipX
    if (isFacingLeftButNotFlipped || isFacingRightButFlipped) {
        flipX()
    }
    return this
}