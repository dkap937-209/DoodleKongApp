package com.dk.doodlekong.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun LifecycleOwner.launchWhenStarted(block: suspend () -> Unit): Job {
    return lifecycleScope.launch {
        var isComplete = false
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            if (!isComplete) {
                try {
                    block()
                } finally {
                    isComplete = true // Ensure block runs only once
                }
            }
        }
    }
}