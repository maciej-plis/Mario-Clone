package com.matthias.mario.sprites.tiles

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Vector2
import com.matthias.mario.common.*
import com.matthias.mario.extensions.getContent
import com.matthias.mario.extensions.getSound
import com.matthias.mario.extensions.toPixels
import com.matthias.mario.screens.GameScreen
import com.matthias.mario.sprites.items.Flower
import com.matthias.mario.sprites.items.Mushroom
import com.matthias.mario.sprites.items.Star
import com.matthias.mario.sprites.tiles.TileContent.*


class MysteryBlock(gameScreen: GameScreen, obj: RectangleMapObject) : InteractiveTile(gameScreen, obj) {

    val content: TileContent = obj.properties.getContent("content", COIN)

    init {
        fixture.userData = this
        fixture.filterData.categoryBits = MYSTERY_BLOCK_BIT
    }

    override fun onHeadCollision() {
        if (cell?.tile?.id == EMPTY_MYSTERY_BLOCK) {
            gameScreen.assetManager.getSound(BUMP_SOUND).play()
            return
        }

        if (content == COIN) {
            gameScreen.assetManager.getSound(COIN_SOUND).play()
        } else if (content == MUSHROOM) {
            Gdx.app.postRunnable {
                gameScreen.items.add(Mushroom(gameScreen, Vector2(body.x.toPixels(), body.y.toPixels() + 16f)))
                gameScreen.assetManager.getSound(POWER_UP_APPEARS_SOUND).play()
            }
        } else if (content == FLOWER) {
            Gdx.app.postRunnable {
                gameScreen.items.add(Flower(gameScreen, Vector2(body.x.toPixels(), body.y.toPixels() + 16f)))
                gameScreen.assetManager.getSound(POWER_UP_APPEARS_SOUND).play()
            }
        } else if (content == STAR) {
            Gdx.app.postRunnable {
                gameScreen.items.add(Star(gameScreen, Vector2(body.x.toPixels(), body.y.toPixels() + 16f)))
                gameScreen.assetManager.getSound(POWER_UP_APPEARS_SOUND).play()
            }
        }

        cell?.tile = gameScreen.map.tileSets.getTile(EMPTY_MYSTERY_BLOCK)
    }
}