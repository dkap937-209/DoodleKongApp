package com.dk.doodlekong.ui.drawing

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.navigation.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.dk.doodlekong.R
import com.dk.doodlekong.data.remote.ws.models.GameError
import com.dk.doodlekong.data.remote.ws.models.GameError.Companion.ERROR_ROOM_NOT_FOUND
import com.dk.doodlekong.data.remote.ws.models.JoinRoomHandshake
import com.dk.doodlekong.databinding.ActivityDrawingBinding
import com.dk.doodlekong.util.Constants.DEFAULT_PAINT_THICKNESS
import com.dk.doodlekong.util.launchWhenStarted
import com.google.android.material.snackbar.Snackbar
import com.tinder.scarlet.WebSocket
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DrawingActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDrawingBinding

    private val viewModel by viewModels<DrawingViewModel>()

    private val args: DrawingActivityArgs by navArgs()

    @Inject
    lateinit var clientId: String

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var rvPlayers: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribeToUiStateUpdates()
        listenToConnectionEvents()
        listenToSocketEvents()

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

        binding.drawingView.setOnDrawListener {
            if(binding.drawingView.isUserDrawing) {
                viewModel.sendBaseModel(it)
            }
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
        launchWhenStarted {
            viewModel.connectionProgressBarVisible.collect { isVisible ->
                binding.connectionProgressBar.isVisible = isVisible
            }
        }
        launchWhenStarted {
            viewModel.chooseWordOverlayVisible.collect { isVisible ->
                binding.chooseWordOverlay.isVisible = isVisible
            }
        }
    }

    private fun listenToSocketEvents() = launchWhenStarted {
        viewModel.socketEvent.collect { event ->
            when(event) {
                is DrawingViewModel.SocketEvent.GameErrorEvent -> {
                    when(event.data.errorType) {
                       ERROR_ROOM_NOT_FOUND -> finish()
                    }
                }
                else -> Unit
            }
        }
    }

    private fun listenToConnectionEvents() = launchWhenStarted {
        viewModel.connectionEvent.collect { event ->
            when(event) {
                is WebSocket.Event.OnConnectionOpened<*> -> {
                    viewModel.sendBaseModel(
                        JoinRoomHandshake(
                            args.username,
                            args.roomName,
                            clientId
                        )
                    )
                    viewModel.setConnectionProgressBarVisibility(false)
                }
                is WebSocket.Event.OnConnectionClosed -> {
                    viewModel.setConnectionProgressBarVisibility(false)
                }
                is WebSocket.Event.OnConnectionFailed -> {
                    viewModel.setConnectionProgressBarVisibility(false)
                    Snackbar.make(
                        binding.root,
                        R.string.error_connection_failed,
                        Snackbar.LENGTH_LONG
                    ).show()
                    event.throwable.printStackTrace()
                }
                else -> Unit
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