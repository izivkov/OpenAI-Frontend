package org.avmedia.openaifrontend

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.avmedia.openaifrontend.databinding.ActivityMainBinding
import org.avmedia.openaifrontend.ui.common_fragments.ApiKeyDialog
import org.avmedia.openaifrontend.utils.LocalDataStorage

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
        }

        dialogFragment.show(supportFragmentManager, "API Key")
    }

    companion object {
        private var instance: MainActivity? = null

        // Make context available from anywhere in the code (not yet used).
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}