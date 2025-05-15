package com.dk.doodlekong.repository

import com.dk.doodlekong.data.remote.ws.Room
import com.dk.doodlekong.util.Resource

interface SetupRepository {

    suspend fun createRoom(room: Room): Resource<Unit>

    suspend fun getRooms(searchQuery: String): Resource<List<Room>>

    suspend fun joinRoom(username: String, roomName: String): Resource<Unit>
}