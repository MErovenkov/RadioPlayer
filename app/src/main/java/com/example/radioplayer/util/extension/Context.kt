package com.example.radioplayer.util.extension

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build

fun Context.isDarkThemeOn(): Boolean {
    return resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}

fun Context.foregroundStartService(intent: Intent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this.startForegroundService(intent)
    } else this.startService(intent)
}