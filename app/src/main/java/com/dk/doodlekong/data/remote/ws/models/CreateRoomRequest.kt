package com.dk.doodlekong.data.remote.ws.models

data class CreateRoomRequest(
    val name: String,
    val maxPlayers: Int
)
