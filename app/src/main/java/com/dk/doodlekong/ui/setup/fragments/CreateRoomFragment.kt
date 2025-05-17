package com.dk.doodlekong.ui.setup.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dk.doodlekong.R
import com.dk.doodlekong.databinding.FragmentCreateRoomBinding
import com.dk.doodlekong.ui.setup.SetupViewModel
import com.dk.doodlekong.util.launchWhenStarted
import com.dk.doodlekong.util.snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateRoomFragment: Fragment(R.layout.fragment_create_room) {

    private var _binding: FragmentCreateRoomBinding? = null
    private val binding: FragmentCreateRoomBinding
        get() = _binding!!



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateRoomBinding.bind(view)
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}