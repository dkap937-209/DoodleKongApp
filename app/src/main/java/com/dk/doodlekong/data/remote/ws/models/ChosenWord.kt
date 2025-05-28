package com.dk.doodlekong.data.remote.ws.models

import com.dk.doodlekong.util.Constants.TYPE_CHOSEN_WORD

data class ChosenWord(
    val chosenWord: String,
    val roomName: String
): BaseModel(TYPE_CHOSEN_WORD)
