package com.pk.readersappjetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.pk.readersappjetpack.navigation.ReaderNavigation
import com.pk.readersappjetpack.ui.theme.ReadersAppJetPackTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Email: prasanna@gmail.com
 * Password: 123123123
 * https://www.googleapis.com/books/v1/volumes?q=android
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	companion object {
		val TAG: String = Companion::class.java.simpleName
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			ReadersAppJetPackTheme {
				ReaderApp()
			}
		}
	}
}

@Composable
fun ReaderApp() {
	// A surface container using the 'background' color from the theme
	Surface(color = Color.White) {
		Column(
			modifier = Modifier.fillMaxSize(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			ReaderNavigation()
		}
	}
}