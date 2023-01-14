package com.sanath.simplevideoplayerwithjetpackcompose.di

import com.sanath.simplevideoplayerwithjetpackcompose.presenter.metaDataReader.MetaDataReader
import com.sanath.simplevideoplayerwithjetpackcompose.presenter.metaDataReader.MetaDataReaderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class MetaDataReaderInterfaceModule {
    @Binds
    @ViewModelScoped
    abstract fun bindMetaDataReader(
        metaDataReaderImpl: MetaDataReaderImpl
    ): MetaDataReader
}