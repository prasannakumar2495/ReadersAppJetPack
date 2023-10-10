package com.pk.readersappjetpack.screens.search

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.pk.readersappjetpack.MainActivity.Companion.TAG
import com.pk.readersappjetpack.components.InputField
import com.pk.readersappjetpack.components.ReaderAppBar
import com.pk.readersappjetpack.navigation.ReaderScreens
import io.grpc.android.BuildConfig
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderSearchScreen(navController: NavHostController) {
	Scaffold(
		topBar = {
			ReaderAppBar(
				title = "Search Screen",
				icon = Icons.Default.ArrowBack,
				navController = navController,
				showProfile = false
			) {
				navController.popBackStack()
			}
		}
	) { paddingValues ->
		Surface(modifier = Modifier.padding(paddingValues)) {
			Column {
				SearchForm(
					modifier = Modifier
						.fillMaxWidth()
						.padding(16.dp)
				) {
					Log.d(TAG, "ReaderSearchScreen: $it")
				}
				LazyColumn(contentPadding = PaddingValues(8.dp)) {
					items(10) {
						BooksSingleItem(
							imageUrl = "", bookTitle = "Android JetPack Development",
							authorName = "PK",
							date = LocalDate.now().toString(), subject = "Computer Science",
							navController = navController
						)
					}
				}
			}
		}
	}
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchForm(
	modifier: Modifier = Modifier,
	loading: Boolean = false,
	hint: String = "Search",
	onSearch: (String) -> Unit = {},
) {
	val searchState = rememberSaveable {
		mutableStateOf("")
	}
	val keyboardController = LocalSoftwareKeyboardController.current
	val valid = remember(searchState.value) {
		searchState.value.trim().isNotEmpty()
	}
	Column {
		InputField(valueState = searchState, labelId = "Search", enabled = true,
			onActions = KeyboardActions {
				if (!valid) return@KeyboardActions
				onSearch(searchState.value.trim())
				searchState.value = ""
				keyboardController?.hide()
			}
		)
	}
}

@Composable
fun BooksSingleItem(
	modifier: Modifier = Modifier, imageUrl: String? = "",
	bookTitle: String = "", authorName: String = "",
	date: String = "", subject: String = "", navController: NavHostController,
) {
	Card(
		modifier = modifier
			.padding(4.dp)
			.fillMaxWidth()
			.clickable {
				navController.navigate(route = ReaderScreens.DetailsScreen.name)
			},
		elevation = CardDefaults.cardElevation(4.dp)
	) {
		Row(verticalAlignment = Alignment.CenterVertically) {
			Image(
				painter = rememberAsyncImagePainter(
					model =
					"http://books.google.com/books/content?id=aYpoDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
				),
				contentDescription = "Book Image",
				modifier = Modifier
					.height(150.dp)
					.width(100.dp)
					.padding(horizontal = 4.dp)
			)
			Column {
				Text(
					text = BuildConfig.BUILD_TYPE,
					style = MaterialTheme.typography.titleLarge,
					fontWeight = FontWeight.Bold,
					overflow = TextOverflow.Ellipsis
				)
				Text(
					text = "Author: $authorName",
					style = MaterialTheme.typography.bodyMedium,
					fontWeight = FontWeight.Bold,
					overflow = TextOverflow.Clip
				)
				Text(
					text = "Date: $date",
					style = MaterialTheme.typography.bodyMedium,
					fontWeight = FontWeight.Bold
				)
				Text(
					text = "[$subject]",
					style = MaterialTheme.typography.bodyMedium,
					fontWeight = FontWeight.Bold
				)
			}
		}
	}
}