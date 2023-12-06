package com.nticoding.calorytracker.navigation

import androidx.navigation.NavController
import com.nticoding.core.util.UIEvent

fun NavController.navigate(event: UIEvent.Navigate) {
    this.navigate(event.route)
}