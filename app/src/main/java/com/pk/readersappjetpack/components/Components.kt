package com.pk.readersappjetpack.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.pk.readersappjetpack.model.MBook
import com.pk.readersappjetpack.navigation.ReaderScreens

@Composable
fun AppLogo(modifier: Modifier = Modifier) {
	Text(
		modifier = modifier.padding(bottom = 16.dp),
		text = "Reader's App",
		color = Color.Red.copy(alpha = 0.5f),
		style = MaterialTheme.typography.headlineLarge
	)
}

@Composable
fun EmailInput(
	modifier: Modifier = Modifier,
	emailState: MutableState<String>,
	labelId: String = "Email",
	enabled: Boolean = true,
	imeAction: ImeAction = ImeAction.Next,
	onActions: KeyboardActions = KeyboardActions.Default,
) {
	InputField(
		modifier = modifier,
		valueState = emailState,
		labelId = labelId,
		enabled = enabled,
		keyboardType = KeyboardType.Email,
		imeAction = imeAction,
		onActions = onActions
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
	modifier: Modifier = Modifier,
	valueState: MutableState<String>,
	labelId: String,
	enabled: Boolean,
	isSingleLine: Boolean = true,
	keyboardType: KeyboardType = KeyboardType.Text,
	imeAction: ImeAction = ImeAction.Next,
	onActions: KeyboardActions = KeyboardActions.Default,
) {
	OutlinedTextField(
		value = valueState.value,
		onValueChange = { valueState.value = it },
		label = { Text(text = labelId) },
		singleLine = isSingleLine,
		textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground),
		modifier = modifier
			.padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
			.fillMaxWidth(),
		enabled = enabled,
		keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
		keyboardActions = onActions
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInput(
	modifier: Modifier,
	passwordState: MutableState<String>,
	labelId: String,
	enabled: Boolean,
	passwordVisibility: MutableState<Boolean>,
	imeAction: ImeAction = ImeAction.Done,
	onAction: KeyboardActions = KeyboardActions.Default,
) {
	val visualTransformation =
		if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()
	OutlinedTextField(
		value = passwordState.value,
		onValueChange = {
			passwordState.value = it
		},
		label = { Text(text = labelId) },
		singleLine = true,
		textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground),
		modifier = modifier
			.padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
			.fillMaxWidth(),
		enabled = enabled,
		keyboardOptions = KeyboardOptions(
			keyboardType = KeyboardType.Password,
			imeAction = imeAction
		), visualTransformation = visualTransformation,
		trailingIcon = { PasswordVisibility(passwordVisibility) }, keyboardActions = onAction
	)
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
	val visible = passwordVisibility.value
	IconButton(onClick = { passwordVisibility.value = !visible }) {
		Icons.Default.Close
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderAppBar(
	modifier: Modifier = Modifier,
	icon: ImageVector? = null,
	title: String,
	showProfile: Boolean = true,
	navController: NavController,
	onBackIconPress: () -> Unit = {},
) {
	TopAppBar(
		modifier = modifier,
		title = {
			Row(
				horizontalArrangement = Arrangement.Start,
				verticalAlignment = Alignment.CenterVertically
			) {
				if (showProfile)
					Icon(
						imageVector = Icons.Default.Favorite,
						contentDescription = "profile image",
						modifier = Modifier
							.clip(RoundedCornerShape(12.dp))
							.padding(end = 10.dp)
							.scale(1.1f),
						tint = MaterialTheme.colorScheme.onSecondary
					)
				if (icon != null)
					Icon(
						imageVector = icon,
						contentDescription = "Back Button",
						modifier = Modifier
							.clickable { onBackIconPress() }
							.padding(end = 20.dp),
						tint = MaterialTheme.colorScheme.onSecondary,
					)
				Text(
					text = title,
					color = MaterialTheme.colorScheme.onSecondary,
					style = MaterialTheme.typography.headlineSmall
				)
			}
		},
		actions = {
			if (showProfile)
				IconButton(onClick = {
					FirebaseAuth.getInstance().signOut().run {
						navController.navigate(route = ReaderScreens.LoginScreen.name)
					}
				}) {
					Icon(
						imageVector = Icons.Filled.Logout, contentDescription = "Logout Button",
						tint = MaterialTheme.colorScheme.onSecondary
					)
				}
		},
		colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.secondary)
	)
}

@Composable
fun TitleSection(modifier: Modifier = Modifier, label: String) {
	Surface(modifier = modifier.padding(start = 5.dp, top = 1.dp)) {
		Column {
			Text(
				text = label,
				fontSize = 18.sp,
				fontStyle = FontStyle.Normal,
				textAlign = TextAlign.Left
			)
		}
	}
}

@Composable
fun FABContent(modifier: Modifier = Modifier, onTap: () -> Unit) {
	FloatingActionButton(
		modifier = modifier,
		onClick = { onTap() },
		shape = RoundedCornerShape(50.dp),
		containerColor = MaterialTheme.colorScheme.secondary
	) {
		Icon(
			imageVector = Icons.Default.Add,
			contentDescription = "Add a book.",
			tint = MaterialTheme.colorScheme.onSecondary
		)
	}
}

@Composable
fun BookRating(score: Double) {
	Card(
		elevation = CardDefaults.cardElevation(4.dp)
	) {
		Column(Modifier.padding(4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
			Icon(imageVector = Icons.Filled.Star, contentDescription = "Rating")
			Text(text = score.toString(), style = MaterialTheme.typography.titleSmall)
		}
	}
}

@Composable
fun ListCard(
	modifier: Modifier = Modifier,
	mBook: MBook = MBook(
		title = "Dummy Title",
		author = "Dummy Author",
		notes = "Dummy Notes",
		id = "Dummy ID"
	),
	onDetailsClick: (String) -> Unit = {},
) {
	val context = LocalContext.current
	val resources = context.resources
	val displayMetrics = resources.displayMetrics
	val screenWidth = displayMetrics.widthPixels / displayMetrics.density
	val spacing = 10.dp
	Card(
		shape = RoundedCornerShape(16.dp),
		elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
		colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSecondary),
		modifier = modifier
			.height(240.dp)
			.width(200.dp)
			.clickable {
				mBook.googleBookId?.let { onDetailsClick(it) }
			}
	) {
		Column(
			modifier = Modifier
				.width(screenWidth.dp - (spacing * 2))
				.fillMaxHeight(),
			verticalArrangement = Arrangement.SpaceBetween
		) {
			Row(
				horizontalArrangement = Arrangement.SpaceBetween,
				modifier = Modifier.fillMaxWidth()
			) {
				Image(
					painter = rememberAsyncImagePainter(model = mBook.photoUrl),
					contentDescription = "Book Image",
					modifier = Modifier
						.height(150.dp)
						.width(100.dp)
						.padding(4.dp)
				)
				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					modifier = Modifier
						.height(150.dp)
						.padding(4.dp)
				) {
					IconButton(onClick = {
						//TODO -> Deal with the fav. button click.
					}) {
						Icon(
							imageVector = Icons.Filled.FavoriteBorder,
							contentDescription = "Favourite Button"
						)
					}
					BookRating(score = 3.5)
				}
			}
			mBook.title?.let {
				Text(
					text = it,
					modifier = Modifier.padding(4.dp),
					maxLines = 1,
					overflow = TextOverflow.Ellipsis, // This will add "..." at the end.
					fontWeight = FontWeight.Bold
				)
			}
			mBook.author?.let {
				Text(
					text = it,
					modifier = Modifier.padding(4.dp),
					maxLines = 1,
					overflow = TextOverflow.Ellipsis
				)
			}
			RoundedOppositeCornersButton(
				label = "Reading",
				modifier = Modifier.align(Alignment.End)
			) {
				//TODO => Implement click.
			}
		}
	}
}

@Composable
fun RoundedOppositeCornersButton(
	label: String, modifier: Modifier = Modifier,
	onPressed: () -> Unit = {},
) {
	Surface(
		shape = RoundedCornerShape(topStart = 8.dp, bottomEnd = 16.dp),
		color = MaterialTheme.colorScheme.secondary,
		modifier = modifier.clickable { onPressed() }
	) {
		Text(text = label, modifier.padding(horizontal = 6.dp, vertical = 4.dp))
	}
}