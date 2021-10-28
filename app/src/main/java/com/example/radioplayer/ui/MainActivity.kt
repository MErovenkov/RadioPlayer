package com.example.radioplayer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.radioplayer.di.activity.ActivityComponent
import com.example.radioplayer.ui.navigation.NavigationComponent
import com.example.radioplayer.ui.theme.MainTheme
import com.example.radioplayer.ui.theme.baseDarkPalette
import com.example.radioplayer.ui.theme.baseLightPalette
import com.example.radioplayer.util.extension.getActivityComponent
import com.example.radioplayer.util.extension.isDarkThemeOn
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity: ComponentActivity(), ActivityComponent.Holder {

    override val activityComponent: ActivityComponent by lazy {
        getActivityComponent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainTheme {
                val systemUiController = rememberSystemUiController()
                val navController = rememberNavController()

                systemUiController.setSystemBarsColor(
                    when (isDarkThemeOn()) {
                        true -> baseDarkPalette.primaryBackground
                        false -> baseLightPalette.primaryBackground
                    }
                )

                NavigationComponent(navController, activityComponent)
            }
        }
    }
}