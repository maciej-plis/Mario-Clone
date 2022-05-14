package com.matthias.mario.common

import com.badlogic.gdx.physics.box2d.Fixture

typealias FixturesMap<T> = MutableMap<T, Fixture>

fun enumerateString(string: String, from: Int, to: Int): Array<String> {
    return IntRange(from, to).map { "$string$it" }.toTypedArray()
}