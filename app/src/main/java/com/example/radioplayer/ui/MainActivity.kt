package com.example.radioplayer.ui

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.radioplayer.di.activity.ActivityComponent
import com.example.radioplayer.ui.navigation.Navigation
import com.example.radioplayer.ui.navigation.NavigationComponent
import com.example.radioplayer.ui.theme.MainTheme
import com.example.radioplayer.ui.theme.baseDarkPalette
import com.example.radioplayer.ui.theme.baseLightPalette
import com.example.radioplayer.util.extension.getActivityComponent
import com.example.radioplayer.util.extension.isDarkThemeOn
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity: ComponentActivity(), ActivityComponent.Holder {

    companion object {
        private const val TITLE_RADIO_KEY = "titleRadio"
        private const val OPEN_RADIO_STATION_KEY = "openingRadioStation"

        fun getNewIntent(context: Context, titleRadio: String): Intent {
            return Intent(context, MainActivity::class.java)
                .apply {
                    putExtra(TITLE_RADIO_KEY, titleRadio)
                    flags = FLAG_ACTIVITY_NEW_TASK
                    action = OPEN_RADIO_STATION_KEY
                }
        }
    }

    override val activityComponent: ActivityComponent by lazy {
        getActivityComponent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUiContent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setUiContent(intent)
    }

    private fun setUiContent(intent: Intent?) {
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

                intent?.let { handleIntent(intent, navController) }
            }
        }
    }

    private fun handleIntent(intent: Intent?, navController: NavHostController) {
        if (intent?.action == OPEN_RADIO_STATION_KEY) {
            intent.getStringExtra(TITLE_RADIO_KEY)?.let {
                navController.navigate("${Navigation.RADIO_PLAYER_SCREEN}/${it}") {
                    launchSingleTop = true
                }
            }
        }
    }
}