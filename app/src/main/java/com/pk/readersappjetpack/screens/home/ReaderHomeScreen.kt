package com.pk.readersappjetpack.screens.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.pk.readersappjetpack.MainActivity.Companion.TAG
import com.pk.readersappjetpack.R
import com.pk.readersappjetpack.components.FABContent
import com.pk.readersappjetpack.components.ListCard
import com.pk.readersappjetpack.components.ReaderAppBar
import com.pk.readersappjetpack.components.TitleSection
import com.pk.readersappjetpack.model.MBook
import com.pk.readersappjetpack.navigation.ReaderScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderHomeScreen(
	navController: NavHostController,
	viewModel: HomeScreenViewModel = hiltViewModel(),
) {
	Scaffold(
		topBar = {
			ReaderAppBar(
				title = stringResource(id = R.string.app_name),
				navController = navController
			)
		}, floatingActionButton = {
			FABContent {
				navController.navigate(ReaderScreens.SearchScreen.name)
			}
		}
	) {
		Surface(
			modifier = Modifier
				.padding(it)
				.fillMaxWidth()
		) {
			HomeContent(navController, viewModel = viewModel)
		}
	}
}

@Composable
fun HomeContent(navController: NavHostController, viewModel: HomeScreenViewModel) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(2.dp),
		verticalArrangement = Arrangement.SpaceEvenly,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Row(
			modifier = Modifier
				.align(Alignment.Start)
				.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			val currentUserName =
				if (!FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty())
					FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0)
				else "N/A"
			TitleSection(label = "Your reading \n activity right now...")
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				modifier = Modifier.width(75.dp)
			) {
				Icon(
					imageVector = Icons.Filled.AccountCircle,
					contentDescription = "Profile",
					modifier = Modifier.clickable {
						navController.navigate(ReaderScreens.ReaderStatsScreen.name)
					})
				currentUserName?.let {
					Text(
						text = it,
						color = MaterialTheme.colorScheme.error,
						modifier = Modifier.padding(4.dp), fontSize = 12.sp,
						overflow = TextOverflow.Clip, maxLines = 1
					)
				}
				Divider()
			}
		}
		viewModel.getAllBooksFromDB()
		val currentUser = FirebaseAuth.getInstance().currentUser
		val booksData =
			viewModel.data.collectAsState().value
//				?.filter { mBook ->
//				mBook.id == currentUser?.uid
//			}
		if (booksData.loading == true) {
			CircularProgressIndicator()
			booksData.loading = false
		} else
			booksData.data?.let { data ->
				Log.d(TAG, "HomeContent: $data")
				ReadingRightNowArea(
					books = data, navController = navController
				)
				TitleSection(
					label = "Reading List",
					modifier = Modifier
						.padding(top = 20.dp)
						.align(Alignment.Start)
				)
				BookListArea(listOfBooks = data, navController = navController)
			}
	}
}

@Composable
fun ReadingRightNowArea(books: List<MBook>, navController: NavController) {
	LazyRow {
		items(books) { bookItem ->
			ListCard(mBook = bookItem, modifier = Modifier.padding(4.dp)) {
				navController.navigate(route = ReaderScreens.UpdateScreen.name + "/$it")
			}
		}
	}
}

@Composable
fun BookListArea(listOfBooks: List<MBook>, navController: NavHostController) {
	HorizontalScrollableComponent(listOfBooks) {
		navController.navigate(route = ReaderScreens.UpdateScreen.name + "/$it")
	}
}

@Composable
fun HorizontalScrollableComponent(listOfBooks: List<MBook>, onCardPressed: (String) -> Unit) {
	LazyRow(
		modifier = Modifier
			.fillMaxWidth()
			.height(280.dp)
	) {
		items(listOfBooks) { bookItem ->
			ListCard(mBook = bookItem,
				modifier = Modifier.padding(4.dp),
				onDetailsClick = {
					onCardPressed(it)
				})
		}
	}
}
