package org.avmedia.openaifrontend.ui.common_fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import org.avmedia.openaifrontend.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OutputTextFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InputEditTextFragment : Fragment() {
    private lateinit var editText: EditText

    private var hint: String? = null
    private var maxHeight: Int? = null
    private var textSize: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            hint = it.getString("hint")
            maxHeight = it.getInt("maxHeight")
            textSize = it.getInt("textSize")
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_input_edit_text, container, false)
        editText = view.findViewById(R.id.input_text)
        editText.hint = hint

        val maxHeightInPixels =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, (maxHeight ?: 200).toFloat(), resources.displayMetrics).toInt()
        editText.maxHeight = maxHeightInPixels.toInt()

        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, (textSize ?: 16).toFloat())

        return view
    }

    fun getText(): String {
        return editText.text.toString()
    }

    fun setText(text: String) {
        editText.setText(text)
    }
}