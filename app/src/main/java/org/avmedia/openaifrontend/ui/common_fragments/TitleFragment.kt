package org.avmedia.openaifrontend.ui.common_fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import org.avmedia.openaifrontend.MainActivity
import org.avmedia.openaifrontend.R
import org.avmedia.openaifrontend.utils.LocalDataStorage

class TitleFragment : Fragment() {
    private lateinit var textView: TextView

    private var text: String? = null
    private var textSize: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            text = it.getString("text")
            textSize = it.getInt("textSize")
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_title, container, false)
        textView = view.findViewById(R.id.title_text)
        textView.text = text

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, (textSize ?: 18).toFloat())

        var updateApiKeyButton: Button = view.findViewById(R.id.update_api_key_button)
        updateApiKeyButton.setOnClickListener {
            getApiKey()
        }

        return view
    }

    private fun getApiKey() {
        val dialogFragment = ApiKeyDialog()
        dialogFragment.setOnCancel {
            Log.i("", "Cancel called")
        }

        dialogFragment.setOnSubmit {
            LocalDataStorage.put("apikey", it, requireContext())
        }

        dialogFragment.show((requireContext() as MainActivity).supportFragmentManager, "API Key")
    }

    fun setText(text: String) {
        textView.text = text
    }

    fun setResult(value: String) {
        requireActivity().supportFragmentManager.setFragmentResult(
            "setApiKeyClick",
            bundleOf("resultKey" to value)
        )
    }
}