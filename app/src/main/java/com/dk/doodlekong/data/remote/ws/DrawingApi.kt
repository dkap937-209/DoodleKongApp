package com.dk.doodlekong.data.remote.ws

import com.dk.doodlekong.data.remote.ws.models.BaseModel
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.flow.Flow

interface DrawingApi {

    @Receive
    fun observeEvents(): Flow<WebSocket.Event>

    @Send
    fun sendBaseModel(baseModel: BaseModel): Boolean

    @Receive
    fun observerBaseModels(): Flow<BaseModel>
}