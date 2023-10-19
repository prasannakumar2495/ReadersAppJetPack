package com.pk.readersappjetpack.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class MBook(
	@Exclude
	val id: String? = null,
	val title: String? = null,
	val author: String? = null,
	val notes: String? = null,
	val photoUrl: String? = null,
	val categories: String? = null,
	val publishedData: String? = null,
	val rating: Double? = null,
	val description: String? = null,
	val pageCount: String? = null,
	val startedRecording: Timestamp? = null,
	val finishedRecording: Timestamp? = null,
	val userId: String? = null,
	val googleBookId: String? = null,
)
