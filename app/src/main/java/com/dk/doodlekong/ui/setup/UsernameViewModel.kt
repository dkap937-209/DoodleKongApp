package com.dk.doodlekong.ui.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dk.doodlekong.data.remote.ws.Room
import com.dk.doodlekong.repository.SetupRepository
import com.dk.doodlekong.util.Constants.MAX_ROOM_NAME_LENGTH
import com.dk.doodlekong.util.Constants.MAX_USERNAME_LENGTH
import com.dk.doodlekong.util.Constants.MIN_ROOM_NAME_LENGTH
import com.dk.doodlekong.util.Constants.MIN_USERNAME_LENGTH
import com.dk.doodlekong.util.DispatcherProvider
import com.dk.doodlekong.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsernameViewModel @Inject constructor(
    private val repository: SetupRepository,
    private val dispatchers: DispatcherProvider
): ViewModel() {

    sealed class SetupEvent {
        data object InputEmptyError: SetupEvent()
        data object InputTooShortError: SetupEvent()
        data object InputTooLongError: SetupEvent()

        data class NavigateToSelectRoomEvent(val username: String): SetupEvent()
    }

    private val _setupEvent = MutableSharedFlow<SetupEvent>()
    val setupEvent: SharedFlow<SetupEvent> = _setupEvent.asSharedFlow()

    fun validateUsernameAndNavigateToSelectRoom(username: String) {
        viewModelScope.launch(dispatchers.main) {
            val trimmedUsername = username.trim()
            when {
                trimmedUsername.isEmpty() -> {
                    _setupEvent.emit(SetupEvent.InputEmptyError)
                }
                trimmedUsername.length < MIN_USERNAME_LENGTH -> {
                    _setupEvent.emit(SetupEvent.InputTooShortError)
                }
                trimmedUsername.length > MAX_USERNAME_LENGTH -> {
                    _setupEvent.emit(SetupEvent.InputTooLongError)
                }
                else -> _setupEvent.emit(SetupEvent.NavigateToSelectRoomEvent(username))
            }
        }
    }
}