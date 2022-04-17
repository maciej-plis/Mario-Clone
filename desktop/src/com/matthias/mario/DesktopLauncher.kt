package com.matthias.mario

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

object DesktopLauncher {

    private val config = Lwjgl3ApplicationConfiguration().apply {
        setForegroundFPS(60)
        setWindowedMode(1248, 720)
    }

    @JvmStatic
    fun main(arg: Array<String>) {
        Lwjgl3Application(MarioGame(), config)
    }
}