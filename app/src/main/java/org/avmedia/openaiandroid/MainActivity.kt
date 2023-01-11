package org.avmedia.openaiandroid

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.avmedia.openaiandroid.databinding.ActivityMainBinding
import org.avmedia.openaiandroid.ui.common_fragments.ApiKeyDialog
import org.avmedia.openaiandroid.utils.LocalDataStorage
import org.avmedia.openaiandroid.utils.Utils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    init {
        instance = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_question, R.id.navigation_image, R.id.navigation_summarize_web
            )
        )

        val apiKey = LocalDataStorage.get("apikey", "", this) as String
        if (apiKey == null || apiKey.isEmpty()) {
            getApiKey()
        }

        navView.setupWithNavController(navController)
    }

    private fun getApiKey() {
        val dialogFragment = ApiKeyDialog()
        dialogFragment.setOnCancel {
            Log.i("", "Cancel called")
        }

        dialogFragment.setOnSubmit {
            LocalDataStorage.put("apikey", it, this)
            if (!validateApiKey(it)) {
                Utils.toast(this, "The API key you have entered may not be valid! This App will not work with an invalid key!")
            }
        }

        dialogFragment.show(supportFragmentManager, "API Key")
    }

    private fun validateApiKey(key: String): Boolean {
        // Do more validation here
        // return key.matches("[a-zA-Z0-9]{25}".toRegex())
        return key.length == 51
    }

    companion object {
        private var instance: MainActivity? = null

        // Make context available from anywhere in the code (not yet used).
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}