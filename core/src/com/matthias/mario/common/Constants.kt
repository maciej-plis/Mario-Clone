package com.matthias.mario.common

// Category bits for filtering collisions
const val GROUND_BIT: Short = 1
const val MARIO_BIT: Short = 2
const val MYSTERY_BLOCK_BIT: Short = 4
const val BRICK_BIT: Short = 8
const val DESTROYED_BIT: Short = 16
const val ENEMY_BIT: Short = 32
const val OBJECT_BIT: Short = 64
const val ITEM_BIT: Short = 128

// Tiled maps
const val LEVEL_1_1_MAP = "levels/level_1-1.tmx"

// Tiled Map tiles
const val EMPTY_MYSTERY_BLOCK = 50

// Texture atlas assets
const val MARIO_ATLAS = "mario.atlas"
const val ENEMIES_ATLAS = "enemies.atlas"
const val ITEMS_ATLAS = "items.atlas"

// Music assets
const val OVERWORLD_MUSIC = "audio/music/overworld-music.mp3"

// Sfx assets
const val COIN_SOUND = "audio/sfx/coin.wav"
const val BUMP_SOUND = "audio/sfx/bump.wav"
const val JUMP_SMALL_SOUND = "audio/sfx/jump-small.wav"
const val JUMP_BIG_SOUND = "audio/sfx/jump-super.wav"
const val BRAKE_BLOCK_SOUND = "audio/sfx/breakblock.wav"
const val POWER_UP_APPEARS_SOUND = "audio/sfx/powerup_appears.wav"
const val MARIO_GROW_SOUND = "audio/sfx/powerup.wav"
const val PIPE_SOUND = "audio/sfx/pipe.wav"


