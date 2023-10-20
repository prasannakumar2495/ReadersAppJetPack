package com.pk.readersappjetpack.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pk.readersappjetpack.MainActivity.Companion.TAG
import com.pk.readersappjetpack.data.DataOrException
import com.pk.readersappjetpack.model.MBook
import com.pk.readersappjetpack.repo.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: FireRepository) :
	ViewModel() {
	val data: MutableStateFlow<DataOrException<List<MBook>, Boolean, Exception>> = MutableStateFlow(
		DataOrException(
			listOf(), true, Exception("")
		)
	)
	
	fun getAllBooksFromDB() {
		viewModelScope.launch {
			data.value.loading = true
			data.value = repository.getAllBooksFromDB()
			if (!data.value.data.isNullOrEmpty()) data.value.loading = false
			Log.d(TAG, "getAllBooksFromDB: ${data.value}")
		}
	}
}