package com.ry05k2ulv.myapplication.ui.generate.result

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ry05k2ulv.myapplication.generator.MosaicArtGenerator
import com.ry05k2ulv.myapplication.imagestore.MagImageStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject

const val targetImageUriArg = "targetImageUri"
const val materialImageUrisArg = "materialImageUris"
const val gridSizeArg = "gridSize"
const val outputSizeArg = "outputSize"

class ResultArgs(
    val targetImageUri: Uri,
    val materialImageUris: List<Uri>,
    val gridSize: Int,
    val outputSize: Int,
) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                Uri.parse(checkNotNull(savedStateHandle[targetImageUriArg])),
                (checkNotNull(savedStateHandle[materialImageUrisArg]) as String).split(",")
                    .map { Uri.parse(it) },
                checkNotNull(savedStateHandle[gridSizeArg]),
                checkNotNull(savedStateHandle[outputSizeArg])
            )
}

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val magImageStore: MagImageStore,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val targetImageUri: Uri

    private val materialImageUris: List<Uri>

    private val gridSize: Int

    private val outputSize: Int

    init {
        val args = ResultArgs(savedStateHandle)
        targetImageUri = args.targetImageUri
        materialImageUris = args.materialImageUris
        gridSize = args.gridSize
        outputSize = args.outputSize
    }

    private val _uiState = MutableStateFlow(ResultUiState.default)
    val uiState = _uiState.asStateFlow()

    private fun updateResult(bitmap: Bitmap) {
        _uiState.update {
            it.copy(result = bitmap)
        }
    }

    private fun updateProgress(index: Int) {
        _uiState.update {
            it.copy(progress = (index + 1f) / materialImageUris.size)
        }
    }

    private fun updateRunning(running: Boolean) {
        _uiState.update {
            it.copy(running = running)
        }
    }

    private fun updateUriSaved(saveState: SaveState) {
        _uiState.update {
            it.copy(uriSaved = saveState)
        }
    }

    private val generator = MosaicArtGenerator(
        targetImage = magImageStore.getBitmapOrNull(targetImageUri)
            ?: throw FileNotFoundException(),
        gridSize = gridSize,
        outputSize = outputSize
    )

    init {
        generateMosaicArt()
    }

    private fun generateMosaicArt() {
        viewModelScope.launch(Dispatchers.Default) {
            materialImageUris.forEachIndexed { index, it ->
                generator.applyMaterialImage(
                    magImageStore.getBitmapOrNull(it) ?: return@forEachIndexed
                )
                updateResult(generator.getResultCopy())
                delay(20)
                updateProgress(index)
            }
            updateProgress(materialImageUris.size - 1)
            updateRunning(false)
        }
    }

    fun saveResult(filename: String) {
        if (_uiState.value.uriSaved != SaveState.YET) return
        viewModelScope.launch(Dispatchers.IO) {
            val bitmap = _uiState.value.result
            if (bitmap != null) {
                updateUriSaved(SaveState.SAVING)
                magImageStore.saveBitmapAsPng(
                    bitmap = bitmap,
                    filename = filename
                )
                updateUriSaved(SaveState.SAVED)
            }
        }
    }

    fun saveResultExternal(): Uri {
        val bitmap = uiState.value.result ?: throw IOException("Failed to save result.")
        return magImageStore.saveBitmapExternalAsPng(bitmap)
    }
}

enum class SaveState() {
    YET,
    SAVING,
    SAVED
}

data class ResultUiState(
    val result: Bitmap?,
    val progress: Float,
    val running: Boolean,
    val uriSaved: SaveState,
    val savedExternalUri: SaveState
) {
    companion object {
        val default
            get() = ResultUiState(null, 0f, true, SaveState.YET, SaveState.YET)
    }
}

