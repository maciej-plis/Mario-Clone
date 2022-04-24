package com.matthias.mario.sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.matthias.mario.common.MYSTERY_BLOCK_BIT
import com.matthias.mario.screens.GameScreen

const val GROUND_STONE_TILESET_NAME = "Overworld-tiles"
const val EMPTY_MYSTERY_BLOCK = 50

class MysteryBlock(gameScreen: GameScreen, obj: RectangleMapObject) : InteractiveTile(gameScreen, obj) {

    init {
        fixture.userData = this
        fixture.filterData.categoryBits = MYSTERY_BLOCK_BIT
    }

    override fun onHeadCollision() {
        Gdx.app.log("MysteryBlock", "Mystery Block collided with player!")
        cell?.tile = gameScreen.map.tileSets.getTileSet(GROUND_STONE_TILESET_NAME).getTile(EMPTY_MYSTERY_BLOCK)
    }
}