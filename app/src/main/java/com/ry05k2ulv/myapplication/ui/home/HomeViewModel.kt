package com.ry05k2ulv.myapplication.ui.home

import androidx.lifecycle.ViewModel
import com.ry05k2ulv.myapplication.imagestore.ImageInfo
import com.ry05k2ulv.myapplication.imagestore.MagImageStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val magImageStore: MagImageStore
) : ViewModel() {
    private val _imageInfoList = MutableStateFlow(magImageStore.getImageInfoInGallery())
    val imageInfoList = _imageInfoList.asStateFlow()

    fun refreshImageInfoList() {
        _imageInfoList.update { magImageStore.getImageInfoInGallery() }
    }
}