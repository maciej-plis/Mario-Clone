package com.matthias.mario.sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.matthias.mario.common.MYSTERY_BLOCK_BIT
import com.matthias.mario.screens.GameScreen

class MysteryBlock(gameScreen: GameScreen, obj: RectangleMapObject) : InteractiveTile(gameScreen, obj) {

    init {
        fixture.userData = this
        fixture.filterData.categoryBits = MYSTERY_BLOCK_BIT
    }

    override fun onHeadCollision() {
        Gdx.app.log("MysteryBlock", "Mystery Block collided with player!")
    }
}