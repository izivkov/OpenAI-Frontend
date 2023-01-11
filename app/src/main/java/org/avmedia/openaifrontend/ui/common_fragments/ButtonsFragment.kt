package org.avmedia.openaifrontend.ui.common_fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import org.json.JSONArray

class ButtonsFragment : Fragment() {

    class ButtonDescription(var name: String)

    private lateinit var buttons: Array<ButtonDescription>
    private var buttonDescriptionArray: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            buttonDescriptionArray = it.getString("buttons")
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val buttonsArray = JSONArray(buttonDescriptionArray)

        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.HORIZONTAL

        var layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
        )

        layoutParams.weight = buttonsArray.length().toFloat()

        for (index in 0 until buttonsArray.length()) {
            val btn = com.google.android.material.button.MaterialButton(requireContext())

            val jsonButton = buttonsArray.getJSONObject(index)
            val btnName = jsonButton.getString("name")
            btn.text = btnName

            val btnParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            btnParams.weight = 1f
            btnParams.marginEnd = 20
            btnParams.marginStart = 20
            btn.layoutParams = btnParams

            btn.setOnClickListener {
                setFragmentResult("childToParentInfo", bundleOf("btnName" to btnName))
            }

            linearLayout.addView(btn)
        }

        fun setResult(value: String) {
            requireActivity().supportFragmentManager.setFragmentResult(
                "requestKey",
                bundleOf("resultKey" to value)
            )
        }

        container?.addView(linearLayout, layoutParams)
        return view
    }
}