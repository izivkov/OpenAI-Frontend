package org.avmedia.openaiandroid.ui.draw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.avmedia.openaiandroid.databinding.FragmentDrawBinding

class DrawFragment : Fragment() {

    private var _binding: FragmentDrawBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val drawViewModel =
            ViewModelProvider(this).get(DrawViewModel::class.java)

        _binding = FragmentDrawBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDraw
        drawViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}