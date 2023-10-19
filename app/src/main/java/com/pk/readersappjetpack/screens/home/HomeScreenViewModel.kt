package com.pk.readersappjetpack.screens.home

import androidx.lifecycle.ViewModel
import com.pk.readersappjetpack.repo.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: FireRepository) :
	ViewModel() {
}