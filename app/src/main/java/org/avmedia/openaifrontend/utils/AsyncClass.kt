package org.avmedia.openaifrontend.utils

class AsyncClass {

    private var callback: (() -> Unit)? = null

    fun setCallback(callback: () -> Unit) {
        this.callback = callback
    }

    fun runAsync () {
        Thread {
            callback?.invoke()
        }.start()
    }
}