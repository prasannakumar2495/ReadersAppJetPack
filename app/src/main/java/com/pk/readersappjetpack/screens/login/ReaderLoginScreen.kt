package com.pk.readersappjetpack.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.pk.readersappjetpack.R
import com.pk.readersappjetpack.components.AppLogo
import com.pk.readersappjetpack.components.EmailInput
import com.pk.readersappjetpack.components.PasswordInput
import com.pk.readersappjetpack.navigation.ReaderScreens

@Composable
fun ReaderLoginScreen(
	navController: NavHostController,
	viewModel: LoginScreenViewModel = viewModel(),
) {
	val showLoginForm = rememberSaveable {
		mutableStateOf(true)
	}
	Surface(modifier = Modifier.fillMaxSize()) {
		Column(horizontalAlignment = Alignment.CenterHorizontally) {
			AppLogo()
			if (showLoginForm.value)
				UserForm(loading = false, isCreateAccount = false) { email, pwd ->
					viewModel.signInWithEmailAndPassword(email = email, password = pwd) {
						navController.navigate(ReaderScreens.ReaderHomeScreen.name)
					}
				}
			else UserForm(loading = false, isCreateAccount = true) { email, pwd ->
				viewModel.createUserWithEmailAndPassword(email = email, password = pwd) {
					navController.navigate(ReaderScreens.ReaderHomeScreen.name)
				}
			}
			Row {
				val text = if (showLoginForm.value) "Create Account" else "Login"
				Text(text = "New User?")
				Text(
					text = text, modifier = Modifier
						.clickable {
							showLoginForm.value = !showLoginForm.value
						}
						.padding(horizontal = 10.dp),
					fontWeight = FontWeight.Bold,
					color = MaterialTheme.colorScheme.onSecondaryContainer
				)
			}
		}
	}
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
	loading: Boolean = false,
	isCreateAccount: Boolean = false,
	onDone: (String, String) -> Unit = { _, _ -> },
) {
	val emailData = rememberSaveable { mutableStateOf("") }
	val passwordData = rememberSaveable { mutableStateOf("") }
	val passwordVisibility = rememberSaveable { mutableStateOf(false) }
	val passwordFocusRequest = remember { FocusRequester() }
	val keyBoardController = LocalSoftwareKeyboardController.current
	val valid = remember(emailData.value, passwordData.value) {
		emailData.value.trim().isNotEmpty() && passwordData.value.trim().isNotEmpty()
	}
	val modifier = Modifier
		.height(250.dp)
		.background(MaterialTheme.colorScheme.background)
		.verticalScroll(rememberScrollState())
	
	Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
		if (isCreateAccount) Text(
			text = stringResource(R.string.enter_email_password),
			modifier = Modifier.padding(10.dp)
		)
		EmailInput(emailState = emailData, enabled = !loading, onActions = KeyboardActions {
			passwordFocusRequest.requestFocus()
		})
		PasswordInput(
			modifier = Modifier.focusRequester(passwordFocusRequest),
			passwordState = passwordData,
			labelId = "Password",
			enabled = !loading,
			passwordVisibility = passwordVisibility,
			onAction = KeyboardActions {
				if (!valid) return@KeyboardActions
				onDone(emailData.value.trim(), passwordData.value.trim())
				keyBoardController?.hide()
			}
		)
		SubmitButton(
			textId = if (isCreateAccount) "Create Account" else "Login",
			loading = loading, validInputs = valid
		) {
			onDone(emailData.value.trim(), passwordData.value.trim())
			keyBoardController?.hide()
		}
	}
}

@Composable
fun SubmitButton(textId: String, loading: Boolean, validInputs: Boolean, onClick: () -> Unit) {
	Button(
		onClick = onClick, modifier = Modifier
			.padding(3.dp)
			.fillMaxWidth(), enabled = !loading && validInputs,
		shape = CircleShape
	) {
		if (loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
		else Text(text = textId, modifier = Modifier.padding(5.dp))
	}
}
