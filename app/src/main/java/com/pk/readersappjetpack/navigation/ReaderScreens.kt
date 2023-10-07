package com.pk.readersappjetpack.navigation

enum class ReaderScreens {
	SplashScreen,
	LoginScreen,
	CreateAccount,
	ReaderHomeScreen,
	SearchScreen,
	DetailsScreen,
	UpdateScreen,
	ReaderStatsScreen;
	
	companion object {
		fun fromRoute(route: String?): ReaderScreens = when (route?.substringBefore("/")) {
			SplashScreen.name -> SplashScreen
			LoginScreen.name -> LoginScreen
			CreateAccount.name -> CreateAccount
			ReaderHomeScreen.name -> ReaderHomeScreen
			SearchScreen.name -> SearchScreen
			DetailsScreen.name -> DetailsScreen
			UpdateScreen.name -> UpdateScreen
			ReaderStatsScreen.name -> ReaderStatsScreen
			null -> ReaderHomeScreen
			else -> throw IllegalArgumentException("Route $route is not recognised.")
		}
	}
}