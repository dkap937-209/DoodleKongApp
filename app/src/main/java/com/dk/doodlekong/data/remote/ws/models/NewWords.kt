package com.dk.doodlekong.data.remote.ws.models

import com.dk.doodlekong.data.remote.ws.models.BaseModel
import com.dk.doodlekong.util.Constants.TYPE_NEW_WORDS

data class NewWords(
    val newWords: List<String>
): BaseModel(TYPE_NEW_WORDS)
