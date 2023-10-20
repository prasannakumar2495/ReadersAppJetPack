package com.pk.readersappjetpack.screens.updates

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pk.readersappjetpack.MainActivity.Companion.TAG
import com.pk.readersappjetpack.components.ReaderAppBar
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
				Log.d(TAG, "ReaderUpdatesScreen: ${bookInfo.data}")
				if (bookInfo.loading == true) {
					CircularProgressIndicator()
					bookInfo.loading = false
				} else
					bookInfo.data?.get(0)?.let { it1 -> it1.title?.let { it2 -> Text(text = it2) } }
			}
		}
	}
}