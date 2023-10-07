package com.pk.readersappjetpack.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.pk.readersappjetpack.MainActivity.Companion.TAG
import com.pk.readersappjetpack.model.MUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {
	private val _loadingState = MutableStateFlow(LoadingState.IDLE)
	val loadingState = _loadingState
	private val firebaseAuth: FirebaseAuth = Firebase.auth
	
	fun createUserWithEmailAndPassword(
		email: String,
		password: String,
		navigateToHome: () -> Unit,
	) {
		firebaseAuth.createUserWithEmailAndPassword(email, password)
			.addOnCompleteListener { task ->
				if (task.isSuccessful) {
					val displayName = task.result.user?.email?.split('@')?.get(0)
					createUser(displayName, navigateToHome)
					Log.d(TAG, "createUserWithEmailAndPassword: ${task.result}, $displayName")
				} else {
					Log.d(TAG, "createUserWithEmailAndPassword: ${task.result}")
				}
			}
	}
	
	private fun createUser(displayName: String?, navigateToHome: () -> Unit) {
		val userId = firebaseAuth.currentUser?.uid
		val user = MUser(
			userId = userId.toString(),
			displayName = displayName.toString(),
			avatarUrl = "",
			quote = "Live Young, Wild and Free",
			profession = "Software Developer",
			id = null
		).toMap()
		
		FirebaseFirestore.getInstance().collection("users").add(user).addOnCompleteListener {
			if (it.isSuccessful) navigateToHome()
		}
	}
	
	fun signInWithEmailAndPassword(email: String, password: String, navigateToHome: () -> Unit) =
		viewModelScope.launch {
			try {
				firebaseAuth.signInWithEmailAndPassword(email, password)
					.addOnCompleteListener { task ->
						if (task.isSuccessful) {
							//TODO => Navigate to Home Screen
							Log.d(TAG, "signInWithEmailAndPassword: ${task.result}")
							navigateToHome()
						} else {
							Log.d(TAG, "signInWithEmailAndPassword: ${task.result}")
						}
					}
			} catch (e: Exception) {
				Log.d(TAG, "signInWithEmailAndPassword: ${e.message}")
			}
		}
}