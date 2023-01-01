package org.avmedia.openaiandroid.ui.draw

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DrawViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Draw Fragment"
    }
    val text: LiveData<String> = _text
}