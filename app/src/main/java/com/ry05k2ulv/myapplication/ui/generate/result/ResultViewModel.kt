package com.ry05k2ulv.myapplication.ui.generate.result

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ry05k2ulv.myapplication.generator.MosaicArtGenerator
import com.ry05k2ulv.myapplication.imagestore.MagImageStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import javax.inject.Inject

const val targetImageUriArg = "targetImageUri"
const val materialImageUrisArg = "materialImageUris"
const val gridSizeArg = "gridSize"

class ResultArgs(
    val targetImageUri: Uri,
    val materialImageUris: List<Uri>,
    val gridSize: Int
) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                Uri.parse(checkNotNull(savedStateHandle[targetImageUriArg])),
                (checkNotNull(savedStateHandle[materialImageUrisArg]) as String).split(",")
                    .map { Uri.parse(it) },
                checkNotNull(savedStateHandle[gridSizeArg])
            )
}

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val magImageStore: MagImageStore,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val result = MutableStateFlow<Bitmap?>(null)

    private val targetImageUri: Uri

    private val materialImageUris: List<Uri>

    private val gridSize: Int

    var progress = mutableFloatStateOf(0f)
        private set

    var running = mutableStateOf(true)
        private set

    init {
        val args = ResultArgs(savedStateHandle)
        targetImageUri = args.targetImageUri
        materialImageUris = args.materialImageUris
        gridSize = args.gridSize
    }

    private val generator = MosaicArtGenerator(
        targetImage = magImageStore.getBitmapOrNull(targetImageUri)
            ?: throw FileNotFoundException(),
        gridSize = gridSize
    )

    init {
        generateMosaicArt()
    }

    private fun generateMosaicArt() {
        Log.d("", "size = ${materialImageUris.size}")
        viewModelScope.launch(Dispatchers.Default) {
            materialImageUris.forEachIndexed { index, it ->
                generator.applyMaterialImage(
                    magImageStore.getBitmapOrNull(it) ?: return@forEachIndexed
                )
                result.update { generator.getResultCopy() }
                delay(20)
                progress.value = (index + 1f) / materialImageUris.size
            }
            progress.value = 1f
            running.value = false
        }
    }

    fun saveResult(filename: String) {
        viewModelScope.launch {
            val bitmap = result.value
            if (bitmap != null) {
                magImageStore.saveBitmapAsPng(
                    bitmap = bitmap,
                    filename = filename
                )
            }
        }
    }
}

