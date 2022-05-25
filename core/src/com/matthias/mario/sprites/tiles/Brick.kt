package com.matthias.mario.sprites.tiles

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.matthias.mario.common.BRAKE_BLOCK_SOUND
import com.matthias.mario.common.BRICK_BIT
import com.matthias.mario.extensions.getSound
import com.matthias.mario.screens.GameScreen
import com.matthias.mario.sprites.mario.MarioState
import com.matthias.mario.sprites.mario.MarioState.Companion.MarioStateId.SMALL_MARIO

class Brick(gameScreen: GameScreen, obj: RectangleMapObject) : InteractiveTile(gameScreen, obj) {

    init {
        fixture.userData = this
        fixture.filterData.categoryBits = BRICK_BIT
    }

    override fun onHeadCollision() {
        if(gameScreen.mario.marioState.stateId == SMALL_MARIO) return
        gameScreen.assetManager.getSound(BRAKE_BLOCK_SOUND).play()
        Gdx.app.postRunnable { gameScreen.world.destroyBody(body) }
        cell?.tile = null
    }
}