package com.matthias.mario.common

class EntityState<T>(initialState: T) {

    var currentState = initialState
    var previousState = initialState
    var stateTimer: Float = 0f

    fun update(newState: T, delta: Float) {
        if (newState == currentState) updateStateTimer(delta) else changeState(newState)
    }

    private fun updateStateTimer(delta: Float) {
        stateTimer += delta
    }

    private fun changeState(newState: T) {
        previousState = currentState
        currentState = newState
        stateTimer = 0f
    }
}