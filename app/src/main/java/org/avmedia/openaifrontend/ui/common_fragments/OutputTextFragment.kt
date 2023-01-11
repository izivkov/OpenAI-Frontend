package org.avmedia.openaifrontend.ui.common_fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.avmedia.openaifrontend.R

class OutputTextFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_output_text, container, false)
        textView = view.findViewById(R.id.output_text)
        textView.text = text

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, (textSize ?: 16).toFloat())

        return view
    }

    fun getText(): String {
        return textView.text.toString()
    }
    fun setText(text:String) {
        textView.text = text
    }
}