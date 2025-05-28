package com.dk.doodlekong.data.remote.ws.models

import com.dk.doodlekong.data.remote.ws.models.BaseModel
import com.dk.doodlekong.util.Constants.TYPE_GAME_STATE

data class GameState(
    val drawingPlayer: String,
    val word: String,
): BaseModel(TYPE_GAME_STATE)