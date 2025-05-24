package com.dk.doodlekong.ui.drawing

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dk.doodlekong.R
import com.dk.doodlekong.databinding.ActivityDrawingBinding
import com.dk.doodlekong.util.Constants
import com.dk.doodlekong.util.Constants.DEFAULT_PAINT_THICKNESS
import com.dk.doodlekong.util.launchWhenStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class DrawingActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDrawingBinding

    private val viewModel by viewModels<DrawingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribeToUiStateUpdates()

        binding.colorGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.checkRadioButton(checkedId)
        }
    }

    private fun selectColour(colour: Int) {
        binding.drawingView.setColour(colour)
        binding.drawingView.setThickness(DEFAULT_PAINT_THICKNESS)
    }

    private fun subscribeToUiStateUpdates() {
        launchWhenStarted {
            viewModel.selectedColourButtonId.collect { id ->
                binding.colorGroup.check(id)
                when(id) {
                    R.id.rbEraser -> {
                        binding.drawingView.setColour(Color.WHITE)
                        binding.drawingView.setThickness(40f)
                    }
                   R.id.rbBlack -> selectColour(Color.BLACK)
                   R.id.rbBlue-> selectColour(Color.BLUE)
                   R.id.rbYellow-> selectColour(Color.YELLOW)
                   R.id.rbOrange-> selectColour(getColor(R.color.orange))
                   R.id.rbGreen-> selectColour(Color.GREEN)
                   R.id.rbRed-> selectColour(Color.RED)
                }
            }
        }
    }
}