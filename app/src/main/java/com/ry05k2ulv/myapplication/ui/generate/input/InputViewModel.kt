package com.ry05k2ulv.myapplication.ui.generate.input

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.ry05k2ulv.myapplication.generator.GeneratorConfig
import com.ry05k2ulv.myapplication.generator.GeneratorPriority
import com.ry05k2ulv.myapplication.generator.MosaicArtGenerator
import com.ry05k2ulv.myapplication.generator.OutputExtension
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

const val materialImageLimit = 256

@HiltViewModel
class InputViewModel @Inject constructor() : ViewModel() {
    /* -------------------------
        method for InputScreen
       ------------------------- */

    /* -------------------------------------------
        uiState and method for InputTargetScreen
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
            it.copy(generatorConfig = it.generatorConfig.copy(gridSize = gridSize))
        }
    }

    fun updateOutputExtension(outputExtension: OutputExtension) {
        _targetUiState.update {
            it.copy(generatorConfig = it.generatorConfig.copy(outputExtension = outputExtension, outputSize = GeneratorConfig.DEFAULT_OUTPUT_SIZE))
        }
    }

    fun updateOutputSize(outputSize: Int) {
        _targetUiState.update {
            it.copy(generatorConfig = it.generatorConfig.copy(outputSize = outputSize))
        }
    }

    fun updatePriority(priority: GeneratorPriority) {
        _targetUiState.update {
            it.copy(generatorConfig = it.generatorConfig.copy(priority = priority))
        }
    }


    /* ---------------------------------------------
        uiState and method for InputMaterialScreen
       --------------------------------------------- */
    private val _materialUiState = MutableStateFlow(MaterialUiState.default)
    val materialUiState = _materialUiState.asStateFlow()

    fun addMaterials(uris: List<Uri>) {
        _materialUiState.update {
            it.copy(
                imageUriSet = it.imageUriSet + uris.take(materialImageLimit - it.imageUriSet.size)
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
    val generatorConfig: GeneratorConfig
) {
    companion object {
        val default
            get() = TargetUiState(
                null,
                GeneratorConfig()
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