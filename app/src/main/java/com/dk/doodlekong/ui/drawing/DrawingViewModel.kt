package com.dk.doodlekong.ui.drawing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dk.doodlekong.R
import com.dk.doodlekong.data.remote.ws.DrawingApi
import com.dk.doodlekong.data.remote.ws.models.Announcement
import com.dk.doodlekong.data.remote.ws.models.BaseModel
import com.dk.doodlekong.data.remote.ws.models.ChatMessage
import com.dk.doodlekong.data.remote.ws.models.ChosenWord
import com.dk.doodlekong.data.remote.ws.models.DrawAction
import com.dk.doodlekong.data.remote.ws.models.DrawAction.Companion.ACTION_UNDO
import com.dk.doodlekong.data.remote.ws.models.DrawData
import com.dk.doodlekong.data.remote.ws.models.GameError
import com.dk.doodlekong.data.remote.ws.models.GameState
import com.dk.doodlekong.data.remote.ws.models.NewWords
import com.dk.doodlekong.data.remote.ws.models.Ping
import com.dk.doodlekong.data.remote.ws.models.RoundDrawInfo
import com.dk.doodlekong.util.DispatcherProvider
import com.google.gson.Gson
import com.tinder.scarlet.WebSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.Socket
import javax.inject.Inject

@HiltViewModel
class DrawingViewModel @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val gson: Gson,
    private val drawingApi: DrawingApi
): ViewModel() {

    sealed class SocketEvent {
        data class ChatMessageEvent(val data: ChatMessage): SocketEvent()
        data class AnnouncementEvent(val data: Announcement): SocketEvent()
        data class GameStateEvent(val data: GameState): SocketEvent()
        data class DrawDataEvent(val data: DrawData): SocketEvent()
        data class NewWordsEvent(val data: NewWords): SocketEvent()
        data class ChosenWordEvent(val data: ChosenWord): SocketEvent()
        data class GameErrorEvent(val data: GameError): SocketEvent()
        data class RoundDrawInfoEvent(val data: RoundDrawInfo): SocketEvent()
        object UndoEvent: SocketEvent()
    }

    private val _selectedColourButtonId = MutableStateFlow(R.id.rbBlack)
    val selectedColourButtonId: StateFlow<Int> = _selectedColourButtonId

    private val connectionEventChannel = Channel<WebSocket.Event>()
    val connectionEvent = connectionEventChannel.receiveAsFlow().flowOn(dispatchers.io)

    private val socketEventChannel = Channel<SocketEvent>()
    val socketEventEvent = socketEventChannel.receiveAsFlow().flowOn(dispatchers.io)

    fun checkRadioButton(id: Int) {
        _selectedColourButtonId.value = id
    }

    fun observeEvents() {
        viewModelScope.launch(dispatchers.io) {
            drawingApi.observeEvents().collect { event ->
                connectionEventChannel.send(event)
            }
        }
    }

    fun observeBaseModels() {
        viewModelScope.launch(dispatchers.io) {
            drawingApi.observerBaseModels().collect { data ->
                when(data) {
                    is DrawData -> {
                        socketEventChannel.send(SocketEvent.DrawDataEvent(data))
                    }
                    is DrawAction -> {
                        when(data.action) {
                            ACTION_UNDO -> socketEventChannel.send(SocketEvent.UndoEvent)
                        }
                    }
                    is Ping -> sendBaseModel(Ping())
                }
            }
        }
    }

    fun sendBaseModel(data: BaseModel) {
        viewModelScope.launch(dispatchers.io) {
            drawingApi.sendBaseModel(data)
        }
    }
}