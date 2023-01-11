package org.avmedia.openaifrontend

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class FragmentAdaptor(
    val fragment: Fragment,
    private val bundle: Bundle,
    private val containerId: Int,
    private val fragmentManager: FragmentManager
) {
    class ChildToParentDataHandler(val key: String, val handler: () -> Unit)

    fun create() {
        fragment.arguments = bundle
        fragmentManager.beginTransaction().replace(containerId, fragment).commit()
    }

    companion object {
        fun setListener(
            parent: Fragment,
            dataKey: String,
            dataHandlers: Array<ChildToParentDataHandler>
        ) {
            parent.childFragmentManager.setFragmentResultListener(
                "childToParentInfo",
                parent
            ) { _, bundle ->
                var value = bundle.getString(dataKey)
                dataHandlers.forEach {
                    if (it.key == value) {
                        it.handler.invoke()
                    }
                }
            }
        }
    }
}