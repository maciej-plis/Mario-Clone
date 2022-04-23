package com.matthias.mario.common

import com.badlogic.gdx.physics.box2d.Body

val Body.x: Float
    get() = position.x

val Body.y: Float
    get() = position.y