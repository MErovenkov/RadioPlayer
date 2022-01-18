package com.example.radioplayer.util.state

data class NavigationState<T>(val route: String, val data: T? = null)