package org.avmedia.openaiandroid.ui.question

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.runBlocking
import org.avmedia.openaiandroid.OpenAIConnection
import org.avmedia.openaiandroid.databinding.FragmentQuestionBinding

class QuestionFragment : Fragment() {

    private var _binding: FragmentQuestionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val questionViewModel =
            ViewModelProvider(this).get(QuestionViewModel::class.java)

        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val questionTextView: EditText = binding.questionText
        questionTextView.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                //
            } else {
                //
            }
        }

        questionViewModel.getQuestionEditTextValue().observe(viewLifecycleOwner, Observer<String> {
            questionTextView.setText(it)
        })

        // clear button
        val clearButton = binding.clearButton
        clearButton.setOnClickListener {
            questionViewModel.onClearButtonClicked()
        }

        // submit button
        val submitButton = binding.submitButton
        submitButton.setOnClickListener {

            val questionTextView: EditText = binding.questionText
            val question = questionTextView.text.toString()

            // hide keyboard
            val inputMethodManager = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

            questionViewModel.onSubmitButtonClicked()
            runBlocking {
                if (question.isEmpty()) {
                    questionViewModel.setAnswerTextValue("Please enter a valid question")
                    return@runBlocking
                }
                val data = OpenAIConnection.getDataAsync(question).await()
                questionViewModel.setAnswerTextValue(data)
            }
        }

        // answer text
        val answerTextView: TextView = binding.textViewAnswer
        questionViewModel.getAnswerTextValue().observe(viewLifecycleOwner, Observer<String> {
            answerTextView.setText(it)
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}