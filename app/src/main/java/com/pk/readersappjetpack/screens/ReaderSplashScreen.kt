package com.pk.readersappjetpack.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.pk.readersappjetpack.components.AppLogo
import com.pk.readersappjetpack.navigation.ReaderScreens
import kotlinx.coroutines.delay

@Composable
fun ReadersSplashScreen(navController: NavHostController) {
	val scale = remember {
		Animatable(0f)
	}
	LaunchedEffect(key1 = true) {
		scale.animateTo(
			targetValue = 0.9f,
			animationSpec = tween(
				durationMillis = 800,
				easing = ({
					OvershootInterpolator(8f).getInterpolation(it)
				})
			)
		)
		delay(2000L)
		/**
		 * Validating if the user is already registered.
		 */
		if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty())
			navController.navigate(route = ReaderScreens.LoginScreen.name)
		else
			navController.navigate(route = ReaderScreens.ReaderHomeScreen.name)
	}
	Surface(
		modifier = Modifier
			.padding(20.dp)
			.size(300.dp)
			.scale(scale.value),
		shape = CircleShape,
		color = Color.White,
		border = BorderStroke(1.dp, color = Color.LightGray)
	) {
		Column(
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			AppLogo()
			Text(
				text = "\"Read. Change. yourself\"",
				color = Color.Gray,
				style = MaterialTheme.typography.bodyMedium
			)
		}
	}
}