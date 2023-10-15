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
import android.util.Log
import androidx.core.content.FileProvider
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

        val selection = "${MediaStore.Images.Media.DATA} like ?"

        val selectionArgs = arrayOf("%${Environment.DIRECTORY_PICTURES}/$MAG_DIRECTORY/%")

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME
        )

        contentResolver.query(
            collection, projection, selection, selectionArgs, null
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
    ): Uri {
        val uri: Uri
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
                uri = contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )
                    ?: throw IOException("Failed to insert uri.")
                uri.let { contentResolver.openOutputStream(it) }
                    ?: throw IOException("Failed to open output stream.")
            } else {
                val imageDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val file = File("$imageDir/$MAG_DIRECTORY", filename.suffixPng())
                uri = Uri.fromFile(file)
                file.outputStream()
            }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()
        Log.d(null, uri.toString())
        return uri
    }

    fun saveBitmapExternalAsPng(
        bitmap: Bitmap
    ): Uri {
        val filename = "for_sharing"
        val path = context.getExternalFilesDir(null) ?: throw IOException("Failed to access external files directory.")
        val file = File(path, filename.suffixPng())
//        path.mkdirs()

        if(file.exists()) file.delete()

        val outputStream = file.outputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()
        return FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", file)
    }

    fun deleteImages(uris: Collection<Uri>) {
        uris.forEach {
            contentResolver.delete(it, null, null)
        }
    }

    private fun String.suffixPng(): String =
        if (endsWith(".png", true)) this else "$this.png"

}