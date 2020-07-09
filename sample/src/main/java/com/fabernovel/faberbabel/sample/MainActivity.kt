package com.fabernovel.faberbabel.sample

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.fabernovel.faberbabel.FaberBabel
import com.fabernovel.faberbabel.internal.data.model.Config
import com.fabernovel.statefullayout.showContent
import com.fabernovel.statefullayout.showLoading
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {

    // Must be injected as a Singleton by Dagger with application context
    private val faberBabelSDK: FaberBabel = FaberBabel(this)
    private val fetchLiveData = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        wordingFromFaberbabel.text = this.getString(R.string.hello_world_title)
        wordingNotPresentInFaberbabel.text =
            resources.getString(R.string.wording_not_present_in_faberbabel)
        pluralFromFaberbabel.text = resources.getQuantityString(R.plurals.wording_plural_example, 0)

        wordingButton.setOnClickListener {
            faberbabelStateful.showLoading()
            GlobalScope.launch {
                faberBabelSDK.syncFetchFaberBabelWording(
                    Config(
                        FABERBABEL_SERVICE_URL,
                        PROJECT_ID,
                        Locale.getDefault().language
                    )
                )
                fetchLiveData.postValue(true)
            }
        }
        faberbabelStateful.showContent()

        fetchLiveData.observe(this,
            Observer {
                if (it) {
                    finish()
                    overridePendingTransition(0, 0)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
            })
    }

    override fun attachBaseContext(newBase: Context) {
        // Must be called on application start or when the language is updated
        // The Config object contains url of the faberbabel service, project id  and language code
        faberBabelSDK.asyncFetchFaberBabelWording(
            Config(
                FABERBABEL_SERVICE_URL,
                PROJECT_ID,
                Locale.getDefault().language
            )
        )

        super.attachBaseContext(faberBabelSDK.provideFaberBabelContextWrapper(newBase))
    }

    override fun getResources(): Resources {
        // Fix FaberBabel for devices API 25 and below
        // The returned resources is not the resources that is override in FaberBabel
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            super.getResources()
        } else {
            baseContext.resources
        }
    }

    // These values should be in the config file of the application
    companion object {
        private const val FABERBABEL_SERVICE_URL =
            "https://faberbabel-develop.herokuapp.com/translations/projects"
        private const val PROJECT_ID = "349a41ab-7815-4cf6-ae54-2e5f2304bfe9"
    }
}
