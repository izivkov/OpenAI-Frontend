package org.avmedia.openaifrontend.ui.summarize_web

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SummarizeWebViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Summarize Fragment"
    }
    val text: LiveData<String> = _text
}