package com.pk.readersappjetpack.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pk.readersappjetpack.screens.ReadersSplashScreen
import com.pk.readersappjetpack.screens.createAccount.ReadersCreateAccountScreen
import com.pk.readersappjetpack.screens.deatils.ReaderBookDetailsScreen
import com.pk.readersappjetpack.screens.home.ReaderHomeScreen
import com.pk.readersappjetpack.screens.login.ReaderLoginScreen
import com.pk.readersappjetpack.screens.search.ReaderSearchScreen
import com.pk.readersappjetpack.screens.stats.ReaderStatsScreen
import com.pk.readersappjetpack.screens.updates.ReaderUpdatesScreen
import com.pk.readersappjetpack.util.Constants.BOOK_ID

@RequiresApi(Build.VERSION_CODES.O)
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
			ReaderSearchScreen(navController = navController)
		}
		val detailsScreen = ReaderScreens.DetailsScreen.name
		composable(
			route = "$detailsScreen/{$BOOK_ID}",
			arguments = listOf(navArgument(name = BOOK_ID) {
				type = NavType.StringType
			})
		) { navBackStackEntry ->
			navBackStackEntry.arguments?.getString(BOOK_ID)?.let {
				ReaderBookDetailsScreen(navController, it)
			}
		}
		composable(
			route = ReaderScreens.UpdateScreen.name + "/{$BOOK_ID}",
			arguments = listOf(navArgument(name = BOOK_ID) { type = NavType.StringType })
		) { navBackStackEntry ->
			navBackStackEntry.arguments?.getString(BOOK_ID)?.let {
				ReaderUpdatesScreen(navController, it)
			}
		}
		composable(ReaderScreens.ReaderStatsScreen.name) {
			ReaderStatsScreen(navController)
		}
	}
}