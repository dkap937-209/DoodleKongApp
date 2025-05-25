package com.dk.doodlekong.ui.drawing

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.recyclerview.widget.RecyclerView
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

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var rvPlayers: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribeToUiStateUpdates()

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        toggle.syncState()

        val header = layoutInflater.inflate(R.layout.nav_drawer_header, binding.navView)
        rvPlayers = header.findViewById(R.id.rvPlayers)
        binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        binding.ibPlayers.setOnClickListener{
            binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            binding.root.openDrawer(GravityCompat.START)
        }

        binding.root.addDrawerListener(object: DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) = Unit

            override fun onDrawerOpened(drawerView: View)  = Unit

            override fun onDrawerClosed(drawerView: View) {
                binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            override fun onDrawerStateChanged(newState: Int) = Unit
        })

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}