package com.pk.readersappjetpack.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pk.readersappjetpack.screens.ReadersSplashScreen
import com.pk.readersappjetpack.screens.createAccount.ReadersCreateAccountScreen
import com.pk.readersappjetpack.screens.deatils.ReaderBookDetailsScreen
import com.pk.readersappjetpack.screens.home.ReaderHomeScreen
import com.pk.readersappjetpack.screens.login.ReaderLoginScreen
import com.pk.readersappjetpack.screens.search.ReaderSearchScreen
import com.pk.readersappjetpack.screens.stats.ReaderStatsScreen
import com.pk.readersappjetpack.screens.updates.ReaderUpdatesScreen

@Composable
fun ReaderNavigation() {
	val navController = rememberNavController()
	NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name) {
		composable(ReaderScreens.SplashScreen.name) {
			ReadersSplashScreen(navController)
		}
		composable(ReaderScreens.LoginScreen.name) {
			ReaderLoginScreen(navController)
		}
		composable(ReaderScreens.CreateAccount.name) {
			ReadersCreateAccountScreen(navController)
		}
		composable(ReaderScreens.ReaderHomeScreen.name) {
			ReaderHomeScreen(navController)
		}
		composable(ReaderScreens.SearchScreen.name) {
			ReaderSearchScreen(navController)
		}
		composable(ReaderScreens.DetailsScreen.name) {
			ReaderBookDetailsScreen(navController)
		}
		composable(ReaderScreens.UpdateScreen.name) {
			ReaderUpdatesScreen(navController)
		}
		composable(ReaderScreens.ReaderStatsScreen.name) {
			ReaderStatsScreen(navController)
		}
	}
}