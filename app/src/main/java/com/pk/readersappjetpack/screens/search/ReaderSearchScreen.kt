package com.pk.readersappjetpack.screens.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pk.readersappjetpack.components.InputField
import com.pk.readersappjetpack.components.ReaderAppBar

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
	) {
		Surface(modifier = Modifier.padding(it)) {
			Column {
				SearchForm(modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp))
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
