package com.matthias.mario.sprites

import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.matthias.mario.common.BUMP_SOUND
import com.matthias.mario.common.COIN_SOUND
import com.matthias.mario.common.EMPTY_MYSTERY_BLOCK
import com.matthias.mario.common.MYSTERY_BLOCK_BIT
import com.matthias.mario.extensions.getSound
import com.matthias.mario.screens.GameScreen


class MysteryBlock(gameScreen: GameScreen, obj: RectangleMapObject) : InteractiveTile(gameScreen, obj) {

    init {
        fixture.userData = this
        fixture.filterData.categoryBits = MYSTERY_BLOCK_BIT
    }

    override fun onHeadCollision() {
        if (cell?.tile?.id == EMPTY_MYSTERY_BLOCK) {
            gameScreen.assetManager.getSound(BUMP_SOUND).play()
        } else {
            gameScreen.assetManager.getSound(COIN_SOUND).play()
        }

        cell?.tile = gameScreen.map.tileSets.getTile(EMPTY_MYSTERY_BLOCK)
    }
}