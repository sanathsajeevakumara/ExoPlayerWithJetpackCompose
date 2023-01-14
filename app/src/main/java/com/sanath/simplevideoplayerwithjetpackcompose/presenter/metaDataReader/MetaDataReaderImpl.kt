package com.sanath.simplevideoplayerwithjetpackcompose.presenter.metaDataReader

import android.app.Application
import android.net.Uri
import android.provider.MediaStore
import javax.inject.Inject

class MetaDataReaderImpl @Inject constructor(
    private val app: Application
) : MetaDataReader {
    override fun getMetaDataFromUri(contentUri: Uri): MetaData? {
        if (contentUri.scheme != "content") return null
        val fileName = app.contentResolver
            .query(
                contentUri,
                arrayOf(MediaStore.Video.VideoColumns.DISPLAY_NAME),
                null,
                null,
                null
            )
            ?.use { cursor ->
                val index = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                cursor.getString(index)
            }
        return fileName?.let { mediaFileName ->
            MetaData(
                fileName = Uri.parse(mediaFileName).lastPathSegment ?: return null
            )

        }
    }
}