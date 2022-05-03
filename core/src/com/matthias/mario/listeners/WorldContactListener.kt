package com.matthias.mario.listeners

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.matthias.mario.common.ENEMY_BIT
import com.matthias.mario.common.OBJECT_BIT
import com.matthias.mario.sprites.*
import com.matthias.mario.sprites.enemies.Enemy
import com.matthias.mario.sprites.enemies.GOOMBA_HEAD
import com.matthias.mario.sprites.enemies.Goomba
import com.matthias.mario.sprites.tiles.InteractiveTile
import kotlin.experimental.or

class WorldContactListener : ContactListener {

    override fun beginContact(contact: Contact) {
        if(contact.fixtureA.userData == MARIO_HEAD || contact.fixtureB.userData == MARIO_HEAD) {
            val marioHead = if (contact.fixtureA.userData == MARIO_HEAD) contact.fixtureA else contact.fixtureB
            val other = if (marioHead == contact.fixtureA) contact.fixtureB else contact.fixtureA
            if(other.userData != null && other.userData is InteractiveTile) {
                (other.userData as InteractiveTile).onHeadCollision()
            }
        } else if((contact.fixtureA.userData == GOOMBA_HEAD || contact.fixtureB.userData == GOOMBA_HEAD) && (contact.fixtureA.userData == MARIO_FEET || contact.fixtureB.userData == MARIO_FEET)) {
            val goombaHead = if (contact.fixtureA.userData == GOOMBA_HEAD) contact.fixtureA else contact.fixtureB
            (goombaHead.body.userData as Goomba).onHeadHit()
        }

        val cDef = contact.fixtureA.filterData.categoryBits or contact.fixtureB.filterData.categoryBits

        when(cDef) {
            ENEMY_BIT, ENEMY_BIT or OBJECT_BIT -> {
                if (contact.fixtureA.filterData.categoryBits == ENEMY_BIT) {
                    (contact.fixtureA.body.userData as Enemy).turnAround()
                }
                if (contact.fixtureB.filterData.categoryBits == ENEMY_BIT) {
                    (contact.fixtureB.body.userData as Enemy).turnAround()
                }
            }
        }

    }

    override fun endContact(contact: Contact) {
    }

    override fun preSolve(contact: Contact, oldManifold: Manifold) {
    }

    override fun postSolve(contact: Contact, impulse: ContactImpulse) {
    }
}
