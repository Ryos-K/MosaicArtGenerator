package com.ry05k2ulv.myapplication.imagestore

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.IOException
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

data class ImageInfo(
    val uri: Uri,
    val filename: String
)

@Singleton
class MagImageStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    companion object {
        const val MAG_DIRECTORY = "MosaicArtGenerator"
    }

    private val contentResolver = context.contentResolver
    fun getBitmapOrNull(
        uri: Uri
    ): Bitmap? {
        return kotlin.runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val source = ImageDecoder.createSource(contentResolver, uri)
                ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.ARGB_8888, true)
            } else {
                MediaStore.Images.Media.getBitmap(contentResolver, uri)
            }
        }.getOrNull()
    }

    fun getImageInfoInGallery(): List<ImageInfo> {
        val imageInfoList = mutableListOf<ImageInfo>()

        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME
        )

        contentResolver.query(
            collection,
            projection,
            null, null, null
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)

                val uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                imageInfoList += ImageInfo(uri, name)
            }
        }
        return imageInfoList
    }

    fun saveBitmapAsPng(
        bitmap: Bitmap,
        filename: String,
    ) {
        val outputStream: OutputStream =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename.suffixPng())
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    "${Environment.DIRECTORY_PICTURES}/$MAG_DIRECTORY"
                )
            }
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                ?.let { contentResolver.openOutputStream(it) }
                ?: throw IOException("Failed to open output stream.")
        } else {
            val imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val file = File("$imageDir/$MAG_DIRECTORY", filename.suffixPng())
            file.outputStream()
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()
    }

    private fun String.suffixPng(): String =
        if (endsWith(".png", true)) this else "$this.png"

}