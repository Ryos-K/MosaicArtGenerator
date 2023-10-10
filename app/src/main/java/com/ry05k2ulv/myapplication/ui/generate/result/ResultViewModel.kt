package com.ry05k2ulv.myapplication.ui.generate.result

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ry05k2ulv.myapplication.generator.MosaicArtGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
                (checkNotNull(savedStateHandle[targetImageUriArg]) as String).split(",").map { Uri.parse(it) },
                checkNotNull(savedStateHandle[gridSizeArg])
            )
}

@HiltViewModel
class ResultViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val result = MutableStateFlow<Bitmap?>(null)

    private val targetImageUri: Uri

    private val materialImageUris: List<Uri>

    private val gridSize: Int

    init {
        val args = ResultArgs(savedStateHandle)
        targetImageUri = args.targetImageUri
        materialImageUris = args.materialImageUris
        gridSize = args.gridSize
    }

    private val generator = MosaicArtGenerator(
        targetImage = targetImageUri.getBitmapOrNull(context) ?: throw FileNotFoundException(),
        gridSize = gridSize
    )

    init {
        generateMosaicArt()
    }

    fun generateMosaicArt() {
        viewModelScope.launch(Dispatchers.Default) {
            materialImageUris.forEach {
                generator.applyMaterialImage(
                    it.getBitmapOrNull(context) ?: return@forEach
                )
                result.update { generator.getResultCopy() }
                delay(20)
            }
        }
    }
}

fun Uri.getBitmapOrNull(
    context: Context
): Bitmap? {
    Log.d(null, "uri to bitmap")
    val contentResolver = context.contentResolver
    return kotlin.runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val source = ImageDecoder.createSource(contentResolver, this)
            ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.ARGB_8888, true)
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, this)
        }
    }.getOrNull()
}