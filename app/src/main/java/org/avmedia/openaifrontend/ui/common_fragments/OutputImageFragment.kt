package org.avmedia.openaifrontend.ui.common_fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import org.avmedia.openaifrontend.R

class OutputImageFragment : Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var statusTextView: TextView

    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            imageUrl = it.getString("imageUrl")
        }
    }

    private fun ImageView.loadImage(url: String?){
        Glide.with(requireContext()).load(url).into(this)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_output_image, container, false)
        imageView = view.findViewById(R.id.output_image)
        statusTextView = view.findViewById(R.id.textview_status)

        return view
    }

    fun setImage (imageUrl:String) {
        imageView.loadImage (imageUrl)
    }

    fun setError(error: String) {
        statusTextView.text = error
    }
}