package com.matthias.mario.listeners

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.matthias.mario.sprites.InteractiveTile

class WorldContactListener : ContactListener {

    override fun beginContact(contact: Contact) {
        if(contact.fixtureA.userData == "head" || contact.fixtureB.userData == "head") {
            val head = if (contact.fixtureA.userData == "head") contact.fixtureA else contact.fixtureB
            val other = if (head == contact.fixtureA) contact.fixtureB else contact.fixtureA
            if(other.userData != null && other.userData is InteractiveTile) {
                (other.userData as InteractiveTile).onHeadCollision()
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
