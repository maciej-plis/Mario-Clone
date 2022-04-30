package com.matthias.mario.sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.matthias.mario.common.BRAKE_BLOCK_SOUND
import com.matthias.mario.common.BRICK_BIT
import com.matthias.mario.extensions.getSound
import com.matthias.mario.screens.GameScreen

class Brick(gameScreen: GameScreen, obj: RectangleMapObject) : InteractiveTile(gameScreen, obj) {

    init {
        fixture.userData = this
        fixture.filterData.categoryBits = BRICK_BIT
    }

    override fun onHeadCollision() {
        gameScreen.assetManager.getSound(BRAKE_BLOCK_SOUND).play()
        Gdx.app.postRunnable { gameScreen.world.destroyBody(body) }
        cell?.tile = null
    }
}