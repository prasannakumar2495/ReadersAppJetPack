package com.pk.readersappjetpack.screens.updates

import android.view.MotionEvent
import android.widget.RatingBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.pk.readersappjetpack.components.InputField
import com.pk.readersappjetpack.components.ReaderAppBar
import com.pk.readersappjetpack.data.DataOrException
import com.pk.readersappjetpack.model.MBook
import com.pk.readersappjetpack.screens.home.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderUpdatesScreen(
	navController: NavHostController, bookId: String,
	viewModel: HomeScreenViewModel = hiltViewModel(),
) {
	Scaffold(
		topBar = {
			ReaderAppBar(
				title = "Update Screen",
				navController = navController,
				icon = Icons.Default.ArrowBack,
				showProfile = false,
			) {
				navController.popBackStack()
			}
		}
	) {
		viewModel.getAllBooksFromDB()
		val bookInfo = viewModel.data.collectAsState().value
		
		Surface(
			modifier = Modifier
				.padding(it)
				.fillMaxSize()
				.padding(3.dp)
		) {
			Column(
				Modifier.padding(top = 4.dp),
				verticalArrangement = Arrangement.Top,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				if (bookInfo.loading == true) {
					CircularProgressIndicator()
					bookInfo.loading = false
				} else {
					ShowBookInfo(bookInfo, bookId = bookId)
					EnterThoughts(bookInfo, bookId, navController)
					InfoAboutReadingTexts(bookInfo = bookInfo, bookId = bookId)
					BookRatingInfo(bookInfo = bookInfo, bookId = bookId)
				}
			}
		}
	}
}

@Composable
private fun ShowBookInfo(
	bookInfo: DataOrException<List<MBook>, Boolean, Exception>,
	bookId: String,
) {
	val bookItemData = bookInfo.data?.first { mBook ->
		mBook.googleBookId == bookId
	}
	Surface(
		modifier = Modifier
			.fillMaxWidth()
			.height(100.dp)
			.padding(10.dp),
		shape = CircleShape,
		shadowElevation = 4.dp
	) {
		Row(
			modifier = Modifier
				.fillMaxSize()
				.padding(4.dp),
			horizontalArrangement = Arrangement.SpaceAround,
			verticalAlignment = Alignment.CenterVertically
		) {
			Image(
				painter = rememberAsyncImagePainter(
					model = bookItemData?.photoUrl, contentScale = ContentScale.Fit
				),
				contentDescription = "Book Image",
				modifier = Modifier
					.fillMaxHeight()
					.width(150.dp)
			)
			Column {
				Text(
					text = bookItemData?.title!!,
					maxLines = 1,
					style = MaterialTheme.typography.titleMedium,
					overflow = TextOverflow.Ellipsis
				)
				Text(
					text = bookItemData.author!!,
					maxLines = 1,
					overflow = TextOverflow.Ellipsis
				)
				Text(
					text = bookItemData.publishedData!!,
					overflow = TextOverflow.Ellipsis
				)
			}
		}
	}
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EnterThoughts(
	bookInfo: DataOrException<List<MBook>, Boolean, Exception>,
	bookId: String,
	navController: NavHostController,
	defaultValue: String = "Initial Value",
) {
	val textFieldValue = rememberSaveable {
		mutableStateOf(defaultValue)
	}
	val keyBoardController = LocalSoftwareKeyboardController.current
	val valid = remember(true) {
		textFieldValue.value.trim().isNotEmpty()
	}
	InputField(
		valueState = textFieldValue, labelId = "Enter Your Thoughts", enabled = valid,
		onActions = KeyboardActions {
			if (valid) return@KeyboardActions
			keyBoardController?.hide()
		}, modifier = Modifier
			.fillMaxWidth()
			.height(150.dp)
			.padding(4.dp)
			.background(color = Color.White, shape = CircleShape)
	)
}

@Composable
fun InfoAboutReadingTexts(
	bookInfo: DataOrException<List<MBook>, Boolean, Exception>,
	bookId: String,
) {
	val bookItem = bookInfo.data?.first { mBook ->
		mBook.googleBookId == bookId
	}
	Row(
		Modifier
			.fillMaxWidth()
			.padding(4.dp),
		horizontalArrangement = Arrangement.SpaceAround,
		verticalAlignment = Alignment.CenterVertically
	) {
		if (bookItem != null) {
			TextButton(onClick = { /*TODO*/ }, enabled = bookItem.startedRecording == null) {
				Text(
					text = "Start Reading",
					style = MaterialTheme.typography.titleLarge,
					color = Color.Blue,
				)
			}
		}
		if (bookItem != null) {
			TextButton(onClick = { /*TODO*/ }, enabled = bookItem.finishedRecording == null) {
				Text(
					text = "Mark As Read",
					style = MaterialTheme.typography.titleLarge,
					color = Color.Blue,
				)
			}
		}
	}
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BookRatingInfo(bookInfo: DataOrException<List<MBook>, Boolean, Exception>, bookId: String) {
	val bookItem = bookInfo.data?.first { mBook ->
		mBook.googleBookId == bookId
	}
	bookItem?.let {
		Text(text = "Rating", style = MaterialTheme.typography.bodyLarge)
		Row {
			for (i in 1 .. 5) {
				IconButton(onClick = { /*TODO*/ }) {
					Icon(imageVector = Icons.Default.StarBorder, "Rating",
						modifier = Modifier.pointerInteropFilter {
							when (it.action) {
								MotionEvent.ACTION_DOWN -> {}
								MotionEvent.ACTION_UP -> {}
							}
							true
						})
				}
			}
		}
	}
}