package com.dk.doodlekong.data.remote.ws.models

import com.dk.doodlekong.data.remote.ws.models.BaseModel
import com.dk.doodlekong.util.Constants.TYPE_DRAW_ACTION

data class DrawAction(
    val action: String
): BaseModel(TYPE_DRAW_ACTION) {

    companion object {
        const val ACTION_UNDO = "ACTION_UNDO"
    }
}
