package com.matthias.mario.sprites

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import com.matthias.mario.MarioGame.Companion.PPM

class Mario(private val world: World) : Sprite() {

    val body: Body

    init {
        val bodyDef = BodyDef().apply {
            position.set(64f / PPM, 64f / PPM)
            type = DynamicBody
        }
        body = world.createBody(bodyDef)

        val fixtureDef = FixtureDef()
        val shape = CircleShape().apply {
            radius = 6f / PPM
        }

        fixtureDef.shape = shape
        body.createFixture(fixtureDef)
    }
}