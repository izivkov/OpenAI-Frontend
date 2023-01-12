package org.avmedia.openaifrontend.ui.image

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import kotlinx.coroutines.runBlocking
import org.avmedia.openaifrontend.FragmentAdaptor
import org.avmedia.openaifrontend.OpenAIConnection
import org.avmedia.openaifrontend.R
import org.avmedia.openaifrontend.databinding.FragmentImageBinding
import org.avmedia.openaifrontend.ui.common_fragments.ButtonsFragment
import org.avmedia.openaifrontend.ui.common_fragments.InputEditTextFragment
import org.avmedia.openaifrontend.ui.common_fragments.OutputImageFragment
import org.avmedia.openaifrontend.ui.common_fragments.TitleFragment
import org.json.JSONObject

class ImageFragment : Fragment() {

    private var _binding: FragmentImageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var inputEditTextFragment: InputEditTextFragment
    private lateinit var outputImageFragment: OutputImageFragment
    private lateinit var buttonsFragment: ButtonsFragment
    private lateinit var titleFragment: TitleFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inputEditTextFragment = InputEditTextFragment()
        outputImageFragment = OutputImageFragment()
        buttonsFragment = ButtonsFragment()
        titleFragment = TitleFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        createChildFragments()

        _binding = FragmentImageBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setButtonHandlers()
        return root
    }

    private fun setButtonHandlers() {
        var childToParentHandlerBundle: Array<FragmentAdaptor.ChildToParentDataHandler> = arrayOf(
            FragmentAdaptor.ChildToParentDataHandler("Clear", ::handleClearButton),
            FragmentAdaptor.ChildToParentDataHandler("Generate Image", ::handleImageButton),
        )
        FragmentAdaptor.setListener(this, "btnName", childToParentHandlerBundle)
    }

    // Handle button clicks from the ButtonsFragment
    private fun handleClearButton() {
        inputEditTextFragment.setText("")
    }

    private fun handleImageButton() {
        // hide keyboard
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

        runBlocking {
            if (getTextFromEditTextFragment().isEmpty()) {
                return@runBlocking
            }
            val response =
                OpenAIConnection.getImageAsync(getTextFromEditTextFragment()).await()
            val url = JSONObject(response).getString("url")
            val error = JSONObject(response).getString("error")
            outputImageFragment.setError(error)
            outputImageFragment.setImage(url)
        }
    }

    private fun getTextFromEditTextFragment(): String {
        return inputEditTextFragment.getText()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createChildFragments() {
        val bundleTitle = Bundle()
        bundleTitle.clear()
        bundleTitle.putString("text", "Generate Image")
        bundleTitle.putInt("textSize", 16)

        FragmentAdaptor(
            titleFragment,
            bundleTitle,
            R.id.fragment_title_container,
            childFragmentManager
        ).create()

        val bundleQuestion = Bundle()
        bundleQuestion.clear()
        bundleQuestion.putString("hint", "Describe what image you like to generate.")
        bundleQuestion.putInt("maxHeight", 200)
        bundleQuestion.putInt("textSize", 16)
        FragmentAdaptor(
            inputEditTextFragment,
            bundleQuestion,
            R.id.fragment_question_container,
            childFragmentManager
        ).create()

        // Input text (question) fragment
        val bundleButtons = Bundle()
        bundleButtons.clear()
        bundleButtons.putString("buttons", "[{name:\"Clear\"},{name:\"Generate Image\"}]")
        FragmentAdaptor(
            buttonsFragment,
            bundleButtons,
            R.id.fragment_buttons_container,
            childFragmentManager
        ).create()

        // Output text (answer) fragment
        val bundleImage = Bundle()
        bundleImage.clear()
        FragmentAdaptor(
            outputImageFragment,
            bundleImage,
            R.id.fragment_image_container,
            childFragmentManager
        ).create()
    }
}