package org.avmedia.openaiandroid.ui.summarize_web

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.avmedia.openaiandroid.databinding.FragmentSummarizeWebBinding

class SummarizeWebFragment : Fragment() {

    private var _binding: FragmentSummarizeWebBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val summarizeWebViewModel =
            ViewModelProvider(this).get(SummarizeWebViewModel::class.java)

        _binding = FragmentSummarizeWebBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSummarizeWeb
        summarizeWebViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}