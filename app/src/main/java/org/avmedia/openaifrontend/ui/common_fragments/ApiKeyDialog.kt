package org.avmedia.openaifrontend.ui.common_fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import org.avmedia.openaifrontend.R
import org.avmedia.openaifrontend.utils.LocalDataStorage
import org.avmedia.openaifrontend.utils.Utils

class ApiKeyDialog : DialogFragment() {
    private lateinit var onSubmitCallback: (String) -> Unit
    private lateinit var onCancelCallback: () -> Unit
    private var isCancelled = false

    @SuppressLint("DialogFragmentCallbacksDetector")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.enter_api_key_layout, null)
        val apiKeyInput: EditText = view.findViewById(R.id.api_key_input)

        builder.setView(view)

        builder
            .setNegativeButton("Cancel") { _, _ ->
                onCancelCallback.invoke()
            }
            .setPositiveButton("Submit") { _, _ ->
                val apiKey = apiKeyInput.text.toString()
                LocalDataStorage.put("apikey", apiKey, requireContext())
                onSubmitCallback.invoke(apiKey)
                if (!validateApiKey(apiKey)) {
                    Utils.toast(requireContext(), "The API key you have entered may not be valid! This App will not work with an invalid key!")
                }
            }
            .setCancelable(false)

        return builder.create()
    }

    private fun validateApiKey(key: String): Boolean {
        // Do more validation here
        // return key.matches("[a-zA-Z0-9]{25}".toRegex())
        return key.length == 51
    }

    fun setOnSubmit(callback: (String) -> Unit) {
        onSubmitCallback = callback
    }

    fun setOnCancel(callback: () -> Unit) {
        onCancelCallback = callback
    }
}
