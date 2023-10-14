package com.ry05k2ulv.myapplication.ui.generate.select

import android.net.Uri
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class SelectMaterialUiState(
    val imageUriSet: Set<Uri>
)

@HiltViewModel
class SelectViewModel @Inject constructor() : ViewModel() {
    /* -------------------------
        method for SelectScreen
       ------------------------- */
    fun isReady(): Boolean =
        targetImageUri.value != null && materialUiState.value.imageUriSet.isNotEmpty()

    /* -------------------------------------------
        uiState and method for SelectTargetScreen
       -------------------------------------------*/
    var targetImageUri = mutableStateOf<Uri?>(null)
        private set

    var gridSize = mutableIntStateOf(32)

    fun updateTargetImageUri(uri: Uri?) {
        targetImageUri.value = uri
    }


    /* ---------------------------------------------
        uiState and method for SelectMaterialScreen
       --------------------------------------------- */
    private val _materialUiState = MutableStateFlow(SelectMaterialUiState(setOf()))
    val materialUiState = _materialUiState.asStateFlow()

    fun addMaterials(uris: List<Uri>) {
        _materialUiState.update {
            it.copy(
                imageUriSet = it.imageUriSet + uris
            )
        }
    }

    fun removeMaterials(uris: Set<Uri>) {
        _materialUiState.update {
            it.copy(
                imageUriSet = it.imageUriSet - uris
            )
        }
    }
}