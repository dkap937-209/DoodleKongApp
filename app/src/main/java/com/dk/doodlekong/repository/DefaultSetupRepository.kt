package com.dk.doodlekong.repository

import android.content.Context
import com.dk.doodlekong.R
import com.dk.doodlekong.data.remote.api.SetupApi
import com.dk.doodlekong.data.remote.ws.Room
import com.dk.doodlekong.util.Resource
import com.dk.doodlekong.util.checkForInternetConnection
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class DefaultSetupRepository @Inject constructor(
    private val setupApi: SetupApi,
    private val context: Context
): SetupRepository {
    override suspend fun createRoom(room: Room): Resource<Unit> {
        if(!context.checkForInternetConnection()) {
            return Resource.Error(
                message = context.getString(R.string.error_internet_turned_off)
            )
        }

        val response = try {
            setupApi.createRoom(room)
        }
        catch (e: HttpException) {
            return Resource.Error(
                message =  context.getString(R.string.error_http)
            )
        }
        catch (e: IOException) {
            return Resource.Error(
                message = context.getString(R.string.check_internet_connection)
            )
        }

        return if(response.isSuccessful && response.body()?.successful == true) {
            Resource.Success(Unit)
        } else if(response.body()?.successful == false) {
            Resource.Error(response.body()!!.message!!)
        } else {
            Resource.Error(context.getString(R.string.error_unknown))
        }
    }

    override suspend fun getRooms(searchQuery: String): Resource<List<Room>> {
        if(!context.checkForInternetConnection()) {
            return Resource.Error(
                message = context.getString(R.string.error_internet_turned_off)
            )
        }

        val response = try {
            setupApi.getRooms(searchQuery)
        }
        catch (e: HttpException) {
            return Resource.Error(
                message =  context.getString(R.string.error_http)
            )
        }
        catch (e: IOException) {
            return Resource.Error(
                message = context.getString(R.string.check_internet_connection)
            )
        }

        return if(response.isSuccessful && response.body() != null) {
            Resource.Success(response.body()!!)
        } else {
            Resource.Error(context.getString(R.string.error_unknown))
        }
    }

    override suspend fun joinRoom(username: String, roomName: String): Resource<Unit> {
        if(!context.checkForInternetConnection()) {
            return Resource.Error(
                message = context.getString(R.string.error_internet_turned_off)
            )
        }

        val response = try {
            setupApi.joinRoom(username, roomName)
        }
        catch (e: HttpException) {
            return Resource.Error(
                message =  context.getString(R.string.error_http)
            )
        }
        catch (e: IOException) {
            return Resource.Error(
                message = context.getString(R.string.check_internet_connection)
            )
        }

        return if(response.isSuccessful && response.body()?.successful == true) {
            Resource.Success(Unit)
        } else if(response.body()?.successful == false) {
            Resource.Error(response.body()!!.message!!)
        } else {
            Resource.Error(context.getString(R.string.error_unknown))
        }
    }
}