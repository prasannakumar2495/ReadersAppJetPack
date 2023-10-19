package com.pk.readersappjetpack.screens.search

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.pk.readersappjetpack.components.InputField
import com.pk.readersappjetpack.components.ReaderAppBar
import com.pk.readersappjetpack.data.DataOrException
import com.pk.readersappjetpack.model.Item
import com.pk.readersappjetpack.navigation.ReaderScreens

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderSearchScreen(
	navController: NavHostController,
	booksViewModel: BookSearchViewModel = hiltViewModel(),
) {
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
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center
			) {
				SearchForm(
					modifier = Modifier
						.fillMaxWidth()
						.padding(16.dp),
					booksViewModel = booksViewModel
				) { query ->
					booksViewModel.searchBooks(query)
					Log.d("QUERY", "ReaderSearchScreen: $query")
				}
				val bookData = booksViewModel.listOfBooks.collectAsState()
				Log.d("Test", "ReaderSearchScreen: ${bookData.value}")
				BooksColumn(navController, bookData = bookData)
			}
		}
	}
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun BooksColumn(
	navController: NavHostController,
	bookData: State<DataOrException<List<Item>, Boolean, Exception>>,
) {
	if (bookData.value.loading == true) {
		CircularProgressIndicator()
	} else
		LazyColumn(contentPadding = PaddingValues(8.dp)) {
			bookData.value.data?.let { data ->
				items(data.size) {
					BooksSingleItem(
						navController = navController, bookItem = data[it]
					)
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
	booksViewModel: BookSearchViewModel,
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
	modifier: Modifier = Modifier,
	navController: NavHostController,
	bookItem: Item,
) {
	Card(
		modifier = modifier
			.padding(4.dp)
			.fillMaxWidth()
			.clickable {
				navController.navigate(route = ReaderScreens.DetailsScreen.name + "/${bookItem.id}")
			},
		elevation = CardDefaults.cardElevation(4.dp)
	) {
		Row(verticalAlignment = Alignment.CenterVertically) {
			Image(
				painter = rememberAsyncImagePainter(
					model = bookItem.volumeInfo.imageLinks.smallThumbnail
				),
				contentDescription = "Book Image",
				modifier = Modifier
					.height(150.dp)
					.width(100.dp)
					.padding(horizontal = 4.dp)
			)
			Column {
				Text(
					text = bookItem.volumeInfo.title,
					style = MaterialTheme.typography.titleLarge,
					fontWeight = FontWeight.Bold,
					overflow = TextOverflow.Ellipsis
				)
				Text(
					text = "Author: ${bookItem.volumeInfo.authors}",
					style = MaterialTheme.typography.bodyMedium,
					fontWeight = FontWeight.Bold,
					overflow = TextOverflow.Clip
				)
				Text(
					text = "Date: ${bookItem.volumeInfo.publishedDate}",
					style = MaterialTheme.typography.bodyMedium,
					fontWeight = FontWeight.Bold
				)
				Text(
					text = "[${bookItem.volumeInfo.categories}]",
					style = MaterialTheme.typography.bodyMedium,
					fontWeight = FontWeight.Bold
				)
			}
		}
	}
}