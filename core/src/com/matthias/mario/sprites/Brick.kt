package com.matthias.mario.sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.matthias.mario.common.BRICK_BIT
import com.matthias.mario.common.DESTROYED_BIT
import com.matthias.mario.screens.GameScreen

class Brick(gameScreen: GameScreen, obj: RectangleMapObject) : InteractiveTile(gameScreen, obj) {

    init {
        fixture.userData = this
        fixture.filterData.categoryBits = BRICK_BIT
    }

    override fun onHeadCollision() {
        Gdx.app.log("Brick", "Brick collided with player!")
        fixture.filterData.categoryBits = DESTROYED_BIT
        gameScreen.assetManager.get("audio/sfx/breakblock.wav", Sound::class.java).play()
        cell?.tile = null
    }
}