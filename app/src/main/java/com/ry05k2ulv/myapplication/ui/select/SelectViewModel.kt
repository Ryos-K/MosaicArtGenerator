package com.ry05k2ulv.myapplication.ui.select

import android.net.Uri
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SelectViewModel: ViewModel() {

    var targetImageUri = mutableStateOf<Uri?>(null)
        private set
    var materialImageUriSet = mutableStateOf(setOf<Uri>())
        private set

    var gridSize = mutableIntStateOf(32)

    fun updateTargetImageUri(uri: Uri?) {
        targetImageUri.value = uri
    }

    fun addMaterialImageUri(uris: List<Uri>) {
        materialImageUriSet.value = materialImageUriSet.value.toMutableSet().apply { addAll(uris) }
    }

    fun removeMaterialImageUri(uri: Uri) {
        materialImageUriSet.value = materialImageUriSet.value.toMutableSet().apply { remove(uri) }
    }

}