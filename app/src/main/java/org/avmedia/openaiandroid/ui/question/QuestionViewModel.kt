package org.avmedia.openaiandroid.ui.question

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import org.avmedia.openaiandroid.OpenAIConnection

class QuestionViewModel : ViewModel() {

    private var questionEditTextValue: MutableLiveData<String> = MutableLiveData()

    fun getQuestionEditTextValue(): LiveData<String> {
        return questionEditTextValue
    }

    fun setQuestionEditTextValue(value: String) {
        questionEditTextValue.value = value
    }

    // clear button
    var clearButtonClicked = MutableLiveData<Boolean>()
    fun onClearButtonClicked() {
        clearButtonClicked.value = true
        questionEditTextValue.value = ""
    }

    // submit button
    var submitButtonClicked = MutableLiveData<Boolean>()
    fun onSubmitButtonClicked() {
        submitButtonClicked.value = true
    }

    // output text
    private var answerTextValue: MutableLiveData<String> = MutableLiveData()
    fun getAnswerTextValue(): LiveData<String> {
        return answerTextValue
    }

    fun setAnswerTextValue(value: String) {
        answerTextValue.value = value
    }
}