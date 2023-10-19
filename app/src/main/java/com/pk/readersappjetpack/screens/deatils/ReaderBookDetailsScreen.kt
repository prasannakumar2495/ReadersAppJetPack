package com.pk.readersappjetpack.screens.deatils

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pk.readersappjetpack.MainActivity.Companion.TAG
import com.pk.readersappjetpack.components.ReaderAppBar
import com.pk.readersappjetpack.components.RoundedOppositeCornersButton
import com.pk.readersappjetpack.model.Item
import com.pk.readersappjetpack.model.MBook
import com.pk.readersappjetpack.navigation.ReaderScreens
import com.pk.readersappjetpack.util.Constants.SAVED_BOOKS_COLLECTION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderBookDetailsScreen(
	navController: NavHostController, bookId: String?,
	bookDetailsViewModel: BookDetailsViewModel = hiltViewModel(),
) {
	Scaffold(topBar = {
		ReaderAppBar(
			title = "Book Details Screen",
			navController = navController,
			icon = Icons.Default.ArrowBack, showProfile = false,
		) {
			navController.navigate(route = ReaderScreens.SearchScreen.name)
		}
	}) {
		Surface(
			modifier = Modifier
				.padding(it)
				.fillMaxSize()
		) {
			Column(
				modifier = Modifier.padding(2.dp),
				verticalArrangement = Arrangement.Top,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				bookId?.let {
					bookDetailsViewModel.getBookInfo(bookId)
				}
				val bookData = bookDetailsViewModel.bookInfo.collectAsState()
				bookData.value.data?.let { item ->
					ShowBookDetails(bookData = item, navController = navController)
					Spacer(modifier = Modifier.height(10.dp))
					SaveAndCancelButton(navController = navController, bookData = item)
				} ?: run {
					CircularProgressIndicator()
				}
			}
		}
	}
}

@Composable
fun SaveAndCancelButton(navController: NavHostController, bookData: Item) {
	Row(modifier = Modifier.fillMaxSize()) {
		RoundedOppositeCornersButton(label = "Save") {
			val book = MBook(
				title = bookData.volumeInfo.title,
				author = bookData.volumeInfo.authors.toString(),
				notes = "",
				description = bookData.volumeInfo.description,
				photoUrl = bookData.volumeInfo.imageLinks.thumbnail,
				categories = bookData.volumeInfo.categories.toString(),
				publishedData = bookData.volumeInfo.publishedDate,
				pageCount = bookData.volumeInfo.pageCount.toString(),
				userId = FirebaseAuth.getInstance().currentUser?.uid,
				googleBookId = bookData.id,
			)
			saveToFirebase(book, navController = navController)
		}
		Spacer(modifier = Modifier.width(20.dp))
		RoundedOppositeCornersButton(label = "Cancel") {
			navController.popBackStack()
		}
	}
}

fun saveToFirebase(book: MBook, navController: NavHostController) {
	val db = FirebaseFirestore.getInstance()
	val dbCollection = db.collection(SAVED_BOOKS_COLLECTION)
	if (book.toString().isNotEmpty()) {
		dbCollection.add(book).addOnSuccessListener { docRef ->
			val docId = docRef.id
			dbCollection.document(docId).update(hashMapOf("id" to docId) as Map<String, Any>)
				.addOnCompleteListener { task ->
					if (task.isSuccessful) {
						navController.popBackStack()
					}
				}
		}.addOnFailureListener {
			Log.w(TAG, "saveToFirebase: Error updating doc", it)
		}
	}
}

@Composable
fun ShowBookDetails(
	bookData: Item,
	navController: NavHostController,
) {
	val dataOfBook = bookData.volumeInfo
	
	Card(
		modifier = Modifier.padding(4.dp),
		shape = CircleShape,
		elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
	) {
		Image(
			modifier = Modifier
				.height(80.dp)
				.width(80.dp),
			painter = rememberAsyncImagePainter(model = dataOfBook.imageLinks.thumbnail),
			contentDescription = bookData.volumeInfo.title,
			contentScale = ContentScale.Fit
		)
	}
	Text(text = dataOfBook.title, style = MaterialTheme.typography.titleLarge)
	Text(text = "Authors: " + dataOfBook.authors, style = MaterialTheme.typography.bodyLarge)
	Text(
		text = "Categories: " + dataOfBook.categories,
		style = MaterialTheme.typography.bodyLarge,
		overflow = TextOverflow.Ellipsis,
		maxLines = 3
	)
	Text(text = "Page Count: " + dataOfBook.pageCount, style = MaterialTheme.typography.bodyLarge)
	Text(
		text = "Published: " + dataOfBook.publishedDate, style = MaterialTheme.typography.bodyLarge
	)
	Spacer(modifier = Modifier.height(10.dp))
	val localDims = LocalContext.current.resources.displayMetrics
	Surface(
		modifier = Modifier
			.height(localDims.heightPixels.dp.times(0.09f))
			.padding(4.dp),
		shape = RoundedCornerShape(4.dp),
		border = BorderStroke(width = 1.dp, color = Color.LightGray)
	) {
		LazyColumn {
			item {
				Text(text = dataOfBook.description, modifier = Modifier.padding(4.dp))
			}
		}
	}
}
