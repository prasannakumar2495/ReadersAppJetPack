package com.pk.readersappjetpack.screens.deatils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pk.readersappjetpack.data.DataOrException
import com.pk.readersappjetpack.model.Item
import com.pk.readersappjetpack.repo.BooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(private val repository: BooksRepository) :
	ViewModel() {
	private val _bookInfo: MutableStateFlow<DataOrException<Item, Boolean, Exception>> =
		MutableStateFlow(
			DataOrException()
		)
	val bookInfo: StateFlow<DataOrException<Item, Boolean, Exception>> = _bookInfo
	fun getBookInfo(bookId: String) = viewModelScope.launch {
		repository.getBookInfo(bookId).onEach {
			_bookInfo.value = it
		}.launchIn(viewModelScope)
	}
}