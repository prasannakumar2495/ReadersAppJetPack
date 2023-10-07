package com.pk.readersappjetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.pk.readersappjetpack.navigation.ReaderNavigation
import com.pk.readersappjetpack.ui.theme.ReadersAppJetPackTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

/**
 * Email: prasanna@gmail.com
 * Password: 123123123
 */

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	companion object {
		val TAG = Companion::class.java.simpleName
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			ReadersAppJetPackTheme {
				val fireStoreDB = FirebaseFirestore.getInstance()
				val users: MutableMap<String, Any> = HashMap<String, Any>().apply {
					put("firstName", "Prasanna")
					put("secondName", "Kumar ${Calendar.getInstance().time}")
				}
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