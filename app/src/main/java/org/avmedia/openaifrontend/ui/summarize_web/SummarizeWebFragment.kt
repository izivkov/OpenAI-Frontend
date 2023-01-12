package org.avmedia.openaifrontend.ui.summarize_web

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import androidx.fragment.app.Fragment
import kotlinx.coroutines.runBlocking
import org.avmedia.openaifrontend.FragmentAdaptor
import org.avmedia.openaifrontend.OpenAIConnection
import org.avmedia.openaifrontend.R
import org.avmedia.openaifrontend.databinding.FragmentSummarizeWebBinding
import org.avmedia.openaifrontend.ui.common_fragments.ButtonsFragment
import org.avmedia.openaifrontend.ui.common_fragments.InputEditTextFragment
import org.avmedia.openaifrontend.ui.common_fragments.OutputTextFragment
import org.avmedia.openaifrontend.ui.common_fragments.TitleFragment
import org.avmedia.openaifrontend.utils.AsyncClass
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

class SummarizeWebFragment : Fragment() {

    private var _binding: FragmentSummarizeWebBinding? = null

    private lateinit var inputEditUrlFragment: InputEditTextFragment
    private lateinit var buttonsFragment: ButtonsFragment
    private lateinit var outputTextFragment: OutputTextFragment
    private lateinit var titleFragment: TitleFragment

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inputEditUrlFragment = InputEditTextFragment()
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

        _binding = FragmentSummarizeWebBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setButtonHandlers()
        return root
    }

    private fun setButtonHandlers() {
        var childToParentHandlerBundle: Array<FragmentAdaptor.ChildToParentDataHandler> = arrayOf(
            FragmentAdaptor.ChildToParentDataHandler("Clear", ::handleClearButton),
            FragmentAdaptor.ChildToParentDataHandler("Summarize", ::handleSummarizeButton),
        )
        FragmentAdaptor.setListener(this, "btnName", childToParentHandlerBundle)
    }

    // Handle button clicks from the ButtonsFragment
    private fun handleClearButton() {
        inputEditUrlFragment.setText("")
    }

    private fun handleSummarizeButton() {
        // hide keyboard
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

        val url = getUrlFromEditTextFragment()
        if (url.isEmpty()) {
            outputTextFragment.setText("Please enter a URL")
            return
        }
        val cleanUrl = URLUtil.guessUrl(url)
        if (!URLUtil.isValidUrl(cleanUrl)) {
            outputTextFragment.setText("Please enter a valid URL")
            return
        }

        val asyncClass = AsyncClass()

        asyncClass.setCallback {
            runBlocking {
                try {
                    val document: Document = Jsoup.connect(cleanUrl).get()

                    val article = document.body().text()
                    if (article == null) {
                        outputTextFragment.setText("Cannot summarize this article")
                        return@runBlocking
                    }

                    val response =
                        OpenAIConnection.getDataAsync("${article.trimStringToLength(4000)}\n\nTL;DR")
                            .await()

                    val mainHandler = Handler(Looper.getMainLooper())
                    mainHandler.post {
                        val text = JSONObject(response).getString("text")
                        val error = JSONObject(response).getString("error")
                        if (error.isNotEmpty()) {
                            outputTextFragment.setText(error)
                        } else {
                            outputTextFragment.setText(text)
                        }
                    }
                } catch (e: IOException) {
                    val mainHandler = Handler(Looper.getMainLooper())
                    mainHandler.post {
                        outputTextFragment.setText("This URL is not accessible!")
                    }
                    return@runBlocking
                }
            }
        }

        asyncClass.runAsync()
    }

    private fun String.trimStringToLength(length: Int) : String {
        return if (this.length > length) this.substring(0, length) + "" else this
    }

    private fun createChildFragments() {
        val bundleTitle = Bundle()
        bundleTitle.clear()
        bundleTitle.putString("text", "Summarize Article")
        bundleTitle.putInt("textSize", 16)

        FragmentAdaptor(
            titleFragment,
            bundleTitle,
            R.id.fragment_title_container,
            childFragmentManager
        ).create()

        val bundleQuestion = Bundle()
        bundleQuestion.clear()
        bundleQuestion.putString("hint", "Enter article URL to summarize")
        bundleQuestion.putInt("maxHeight", 200)
        bundleQuestion.putInt("textSize", 16)
        FragmentAdaptor(
            inputEditUrlFragment,
            bundleQuestion,
            R.id.fragment_summarize_url_container,
            childFragmentManager
        ).create()

        // Input text (question) fragment
        val bundleButtons = Bundle()
        bundleButtons.clear()
        bundleButtons.putString("buttons", "[{name:\"Clear\"},{name:\"Summarize\"}]")
        FragmentAdaptor(
            buttonsFragment,
            bundleButtons,
            R.id.fragment_buttons_container,
            childFragmentManager
        ).create()

        // Output text (summarized news) fragment
        val bundleAnswer = Bundle()
        bundleAnswer.clear()
        bundleAnswer.putString("text", "")
        bundleAnswer.putInt("textSize", 16)
        FragmentAdaptor(
            outputTextFragment,
            bundleAnswer,
            R.id.fragment_summarized_container,
            childFragmentManager
        ).create()
    }

    private fun getUrlFromEditTextFragment(): String {
        return inputEditUrlFragment.getText()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}