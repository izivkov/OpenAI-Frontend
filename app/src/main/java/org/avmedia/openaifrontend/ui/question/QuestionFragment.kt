package org.avmedia.openaifrontend.ui.question

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import kotlinx.coroutines.runBlocking
import org.avmedia.openaifrontend.FragmentAdaptor
import org.avmedia.openaifrontend.FragmentAdaptor.ChildToParentDataHandler
import org.avmedia.openaifrontend.OpenAIConnection
import org.avmedia.openaifrontend.R
import org.avmedia.openaifrontend.databinding.FragmentQuestionBinding
import org.avmedia.openaifrontend.ui.common_fragments.ButtonsFragment
import org.avmedia.openaifrontend.ui.common_fragments.InputEditTextFragment
import org.avmedia.openaifrontend.ui.common_fragments.OutputTextFragment
import org.avmedia.openaifrontend.ui.common_fragments.TitleFragment
import org.json.JSONObject

class QuestionFragment : Fragment() {

    private var _binding: FragmentQuestionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var inputEditTextFragment: InputEditTextFragment
    private lateinit var outputTextFragment: OutputTextFragment
    private lateinit var buttonsFragment: ButtonsFragment
    private lateinit var titleFragment: TitleFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inputEditTextFragment = InputEditTextFragment()
        outputTextFragment = OutputTextFragment()
        buttonsFragment = ButtonsFragment()
        titleFragment = TitleFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        createChildFragments()

        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setButtonHandlers()

        return root
    }

    private fun setButtonHandlers() {
        var childToParentHandlerBundle: Array<ChildToParentDataHandler> = arrayOf(
            ChildToParentDataHandler("Clear", ::handleClearButton),
            ChildToParentDataHandler("Submit", ::handleSubmitButton),
        )
        FragmentAdaptor.setListener(this, "btnName", childToParentHandlerBundle)
    }

    // Handle button clicks from the ButtonsFragment
    private fun handleClearButton() {
        inputEditTextFragment.setText("")
    }

    private fun handleSubmitButton() {
        // hide keyboard
        val inputMethodManager =
            context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

        runBlocking {
            if (getTextFromEditTextFragment().isEmpty()) {
                outputTextFragment.setText("Please enter a valid question")
                return@runBlocking
            }
            val response =
                OpenAIConnection.getDataAsync(getTextFromEditTextFragment()).await()

            val text = JSONObject(response).getString("text")
            val error = JSONObject(response).getString("error")
            if (error.isNotEmpty()) {
                outputTextFragment.setText(error)
            } else {
                outputTextFragment.setText(text)
            }
        }
    }

    private fun createChildFragments() {

        val bundleTitle = Bundle()
        bundleTitle.clear()
        bundleTitle.putString("text", "Ask me a question")
        bundleTitle.putInt("textSize", 16)

        FragmentAdaptor(
            titleFragment,
            bundleTitle,
            R.id.fragment_title_container,
            childFragmentManager
        ).create()

        // Input text (question) fragment
        val bundleQuestion = Bundle()
        bundleQuestion.clear()
        bundleQuestion.putString("hint", "Please enter a question here.")
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
        bundleButtons.putString("buttons", "[{name:\"Clear\"},{name:\"Submit\"}]")
        FragmentAdaptor(
            buttonsFragment,
            bundleButtons,
            R.id.fragment_buttons_container,
            childFragmentManager
        ).create()

        // Output text (answer) fragment
        val bundleAnswer = Bundle()
        bundleAnswer.clear()
        bundleAnswer.putString("text", "")
        bundleAnswer.putInt("textSize", 16)
        FragmentAdaptor(
            outputTextFragment,
            bundleAnswer,
            R.id.fragment_answer_container,
            childFragmentManager
        ).create()
    }

    private fun getTextFromEditTextFragment(): String {
        return inputEditTextFragment.getText()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}