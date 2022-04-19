package com.matthias.mario.sprites

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.matthias.mario.MarioGame.Companion.PPM
import com.matthias.mario.common.createBodyWithFixture

class Mario(world: World) : Sprite() {

    val body: Body

    init {
        val bodyDef = BodyDef().apply {
            type = DynamicBody
            position.set(64f / PPM, 64f / PPM)
        }
        val fixtureDef = FixtureDef().apply {
            shape = CircleShape().apply { radius = 6f / PPM }
        }
        body = world.createBodyWithFixture(bodyDef, fixtureDef)
    }
}