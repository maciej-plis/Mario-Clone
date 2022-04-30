package com.matthias.mario.listeners

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.matthias.mario.sprites.*

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
    }

    override fun endContact(contact: Contact) {
    }

    override fun preSolve(contact: Contact, oldManifold: Manifold) {
    }

    override fun postSolve(contact: Contact, impulse: ContactImpulse) {
    }
}
