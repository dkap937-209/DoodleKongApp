package com.dk.doodlekong.data.remote.ws.models

import com.dk.doodlekong.data.remote.ws.models.BaseModel
import com.dk.doodlekong.util.Constants.TYPE_JOIN_ROOM_HANDSHAKE

data class JoinRoomHandshake(
    val username: String,
    val roomName: String,
    val clientId: String
): BaseModel(TYPE_JOIN_ROOM_HANDSHAKE)
