package com.matthias.mario.sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.physics.box2d.World

class MysteryBlock(world: World, obj: RectangleMapObject) : InteractiveTile(world, obj) {

    init {
        fixture.userData = this
    }

    override fun onHeadCollision() {
        Gdx.app.log("MysteryBlock", "Mystery Block collided with player!")
    }
}