package com.matthias.mario.sprites

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.Body
import com.matthias.mario.MarioGame.Companion.PPM
import com.matthias.mario.common.*
import com.matthias.mario.screens.GameScreen
import com.matthias.mario.utils.B2DWorldCreator.BODY_DEF
import com.matthias.mario.utils.B2DWorldCreator.FIXTURE_DEF
import com.matthias.mario.utils.B2DWorldCreator.SHAPE

abstract class InteractiveTile(protected val gameScreen: GameScreen, protected val bounds: Rectangle) {

//    protected val tile: TiledMapTile
    protected val body: Body

    init {
        BODY_DEF.position.set(bounds.centerX / PPM, bounds.centerY / PPM)
        SHAPE.setAsBox(bounds.halfWidth / PPM, bounds.halfHeight / PPM)
        body = gameScreen.world.createBodyWithFixture(BODY_DEF, FIXTURE_DEF)
    }
}