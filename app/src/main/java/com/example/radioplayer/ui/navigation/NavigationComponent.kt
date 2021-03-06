package com.example.radioplayer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.radioplayer.di.activity.ActivityComponent
import com.example.radioplayer.ui.screen.favoritelist.FavoriteMusicListScreen
import com.example.radioplayer.ui.screen.player.RadioPlayerScreen
import com.example.radioplayer.ui.screen.radiolist.RadioListScreen
import com.example.radioplayer.util.extension.buildScreenComponent
import com.example.radioplayer.util.extension.daggerViewModel
import com.example.radioplayer.viewmodel.FavoriteMusicViewModel

@Composable
fun NavigationComponent(navController: NavHostController, activityComponent: ActivityComponent) {

    NavHost(navController, Navigation.RADIO_LIST_SCREEN) {
        composable(Navigation.RADIO_LIST_SCREEN) {
            val radioViewModel = daggerViewModel {
                activityComponent.buildScreenComponent().getRadioViewModel()
            }

            RadioListScreen(navController, radioViewModel)
        }

        composable("${Navigation.RADIO_PLAYER_SCREEN}/{radioTitle}") {
            it.arguments?.getString("radioTitle")?.let { title ->
                val radioPlayerViewModel = daggerViewModel {
                    activityComponent.buildScreenComponent().getRadioPlayerViewModel()
                }

                RadioPlayerScreen(navController, title, radioPlayerViewModel)
            }
        }

        composable("${Navigation.FAVORITE_MUSIC_LIST_SCREEN}/{radioTitle}") {
            it.arguments?.getString("radioTitle")?.let { radioTitle ->
                val favoriteMusicViewModel: FavoriteMusicViewModel = daggerViewModel {
                    activityComponent.buildScreenComponent().getFavoriteMusicViewModel()
                }

                FavoriteMusicListScreen(radioTitle, favoriteMusicViewModel)
            }
        }
    }
}