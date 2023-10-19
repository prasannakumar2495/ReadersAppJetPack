package com.pk.readersappjetpack.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pk.readersappjetpack.data.DataOrException
import com.pk.readersappjetpack.model.Item
import com.pk.readersappjetpack.repo.BooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(private val repository: BooksRepository) :
	ViewModel() {
	private val _listOfBooks: MutableStateFlow<DataOrException<List<Item>, Boolean, Exception>> =
		MutableStateFlow(DataOrException(null, false, Exception("")))
	val listOfBooks: StateFlow<DataOrException<List<Item>, Boolean, Exception>> = _listOfBooks
	
	fun searchBooks(query: String) = viewModelScope.launch(Dispatchers.IO) {
		if (query.isEmpty()) return@launch
		_listOfBooks.value.loading = true
		repository.getBooks(query).onEach {
			_listOfBooks.value = it
		}.launchIn(viewModelScope)
		if (_listOfBooks.value.data.toString().isNotEmpty()) _listOfBooks.value.loading = false
	}
}