package org.avmedia.openaiandroid.ui.question

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.runBlocking
import org.avmedia.openaiandroid.FragmentAdaptor
import org.avmedia.openaiandroid.FragmentAdaptor.*
import org.avmedia.openaiandroid.OpenAIConnection
import org.avmedia.openaiandroid.R
import org.avmedia.openaiandroid.databinding.FragmentQuestionBinding
import org.avmedia.openaiandroid.ui.common_fragments.ButtonsFragment
import org.avmedia.openaiandroid.ui.common_fragments.InputEditTextFragment
import org.avmedia.openaiandroid.ui.common_fragments.OutputTextFragment

class QuestionFragment : Fragment() {

    private var _binding: FragmentQuestionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var inputEditTextFragment: InputEditTextFragment
    private lateinit var outputTextFragment: OutputTextFragment
    private lateinit var buttonsFragment: ButtonsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inputEditTextFragment = InputEditTextFragment()
        outputTextFragment = OutputTextFragment()
        buttonsFragment = ButtonsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        createChildFragments()

        val questionViewModel =
            ViewModelProvider(this).get(QuestionViewModel::class.java)

        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setButtonHandlers ()

        return root
    }

    private fun setButtonHandlers () {
        var childToParentHandlerBundle:Array<ChildToParentDataHandler> = arrayOf(
            ChildToParentDataHandler("Clear", ::handleClearButton),
            ChildToParentDataHandler("Submit", ::handleSubmitButton),
        )
        FragmentAdaptor.setListener (this, buttonsFragment, "btnName", childToParentHandlerBundle)
    }

    // Handle button clicks from the ButtonsFragment
    private fun handleClearButton () {
        inputEditTextFragment.setText("")
    }

    private fun handleSubmitButton () {
        // hide keyboard
        val inputMethodManager =
            context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

        runBlocking {
            if (getTextFromEditTextFragment().isEmpty()) {
                outputTextFragment.setText("Please enter a valid question")
                return@runBlocking
            }
            val data =
                OpenAIConnection.getDataAsync(getTextFromEditTextFragment()).await()
            outputTextFragment.setText(data)
        }
    }

    private fun createChildFragments() {
        val bundleQuestion = Bundle()

        // Input text (question) fragment
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