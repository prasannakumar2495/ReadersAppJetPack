package com.pk.readersappjetpack.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController

@Composable
fun ReaderHomeScreen(navController: NavHostController) {
	Surface {
		Column {
			Text(
				text = "This is Home Screen.",
				fontWeight = FontWeight.Bold,
				style = MaterialTheme.typography.bodyLarge
			)
		}
	}
}