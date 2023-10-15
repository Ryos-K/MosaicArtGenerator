package com.ry05k2ulv.myapplication.ui.generate.select

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.ry05k2ulv.myapplication.generator.MosaicArtGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SelectViewModel @Inject constructor() : ViewModel() {
    /* -------------------------
        method for SelectScreen
       ------------------------- */

    /* -------------------------------------------
        uiState and method for SelectTargetScreen
       -------------------------------------------*/
    private val _targetUiState = MutableStateFlow(
        TargetUiState.default
    )
    val targetUiState = _targetUiState.asStateFlow()

    fun updateTargetImageUri(uri: Uri?) {
        _targetUiState.update {
            it.copy(imageUri = uri)
        }
    }

    fun updateGridSize(gridSize: Int) {
        _targetUiState.update {
            it.copy(gridSize = gridSize)
        }
    }

    fun updateOutputSize(outputSize: Int) {
        _targetUiState.update {
            it.copy(outputSize = outputSize)
        }
    }


    /* ---------------------------------------------
        uiState and method for SelectMaterialScreen
       --------------------------------------------- */
    private val _materialUiState = MutableStateFlow(MaterialUiState.default)
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

data class TargetUiState(
    val imageUri: Uri?,
    val gridSize: Int,
    val outputSize: Int
) {
    companion object {
        val default
            get() = TargetUiState(
                null,
                MosaicArtGenerator.DEFAULT_GRID_SIZE,
                MosaicArtGenerator.DEFAULT_OUTPUT_SIZE
            )
    }
}

data class MaterialUiState(
    val imageUriSet: Set<Uri>
) {
    companion object {
        val default
            get() = MaterialUiState(setOf())
    }
}