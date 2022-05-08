package com.matthias.mario.common

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode.NORMAL
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.matthias.mario.extensions.findRegions

class EntityTextures<T>(private val atlas: TextureAtlas) {

    private val textures: MutableMap<T, Any> = mutableMapOf()

    fun addTexture(state: T, textureName: String) {
        checkIfExists(state)
        textures[state] = atlas.findRegion(textureName)
    }

    fun addAnimation(state: T, frameDuration: Float, vararg textureNames: String, mode: PlayMode = NORMAL) {
        checkIfExists(state)
        this.textures[state] = Animation(frameDuration, atlas.findRegions(textureNames), mode)
    }

    fun getCurrentTexture(state: EntityState<T>, looping: Boolean = false): TextureRegion {
        val texture = textures.getOrElse(state.currentState) {
            throw RuntimeException("Texture for state '$state' is not initialized")
        }

        if (texture is TextureRegion) {
            return texture
        }

        return (texture as Animation<TextureRegion>).getKeyFrame(state.stateTimer, looping)
    }

    private fun checkIfExists(state: T) {
        if (textures.containsKey(state)) {
            throw RuntimeException("Texture for state '$state' is already initialized")
        }
    }
}