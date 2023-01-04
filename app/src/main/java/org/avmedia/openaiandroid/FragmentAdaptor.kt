package org.avmedia.openaiandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class FragmentAdaptor (val fragment:Fragment, private val bundle: Bundle, private val containerId: Int, private val fragmentManager:FragmentManager) {
    fun create () {
        fragment.arguments = bundle
        fragmentManager.beginTransaction().replace(containerId, fragment).commit()
    }
}