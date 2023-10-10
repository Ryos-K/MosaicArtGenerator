package com.ry05k2ulv.myapplication.ui.generate

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ry05k2ulv.myapplication.R
import com.ry05k2ulv.myapplication.generator.MosaicArtGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


@HiltViewModel
class GenerateViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {
    var targetImageUri = mutableStateOf<Uri?>(null)
        private set
    var materialImageUriSet = mutableStateOf(setOf<Uri>())
        private set

    var gridSize = mutableIntStateOf(32)

    var result = MutableStateFlow<Bitmap?>(null)

    fun updateTargetImageUri(uri: Uri?) {
        targetImageUri.value = uri
    }

    fun addMaterialImageUri(uris: List<Uri>) {
        materialImageUriSet.value = materialImageUriSet.value.toMutableSet().apply { addAll(uris) }
    }

    fun removeMaterialImageUri(uri: Uri) {
        materialImageUriSet.value = materialImageUriSet.value.toMutableSet().apply { remove(uri) }
    }

    fun generateMosaicArt() {
        if (targetImageUri.value == null) return
        viewModelScope.launch(Dispatchers.Default) {
            val generator = MosaicArtGenerator(
                targetImageUri.value!!.getBitmapOrNull(context)!!,
                gridSize.value
            )
            materialImageUriSet.value.forEach {
                generator.applyMaterialImage(
                    it.getBitmapOrNull(context)!!
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