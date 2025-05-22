package com.dk.doodlekong.ui.drawing

import androidx.lifecycle.ViewModel
import com.dk.doodlekong.R
import com.dk.doodlekong.util.DispatcherProvider
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DrawingViewModel @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val gson: Gson
): ViewModel() {

    private val _selectedColourButtonId = MutableStateFlow(R.id.rbBlack)
    val selectedColourButtonId: StateFlow<Int> = _selectedColourButtonId

    fun checkRadioButton(id: Int) {
        _selectedColourButtonId.value = id
    }
}