package com.matthias.mario.sprites

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.matthias.mario.MarioGame.Companion.PPM
import com.matthias.mario.common.createBodyWithFixture
import com.matthias.mario.screens.GameScreen

class Mario(private val gameScreen: GameScreen) : Sprite(gameScreen.assetManager.get("mario.atlas", TextureAtlas::class.java).findRegion("mario-1")) {

    val body: Body = defineBody()

    init {
        setBounds(body.position.x / PPM, body.position.y / PPM, width / PPM, height / PPM)
    }

    fun update(delta: Float) {
        setPosition(body.position.x - width / 2, body.position.y - height / 2)
    }

    private fun defineBody(): Body {
        val bodyDef = BodyDef().apply {
            type = DynamicBody
            position.set(64f / PPM, 64f / PPM)
        }
        val fixtureDef = FixtureDef().apply {
            shape = CircleShape().apply { radius = 6f / PPM }
        }
        return gameScreen.world.createBodyWithFixture(bodyDef, fixtureDef)
    }
}