package com.sanath.simplevideoplayerwithjetpackcompose.presenter.metaDataReader

import android.net.Uri

interface MetaDataReader {
    fun getMetaDataFromUri(contentUri: Uri): MetaData?
}