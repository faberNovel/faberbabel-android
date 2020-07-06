package com.fabernovel.faberbabel.sample

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fabernovel.faberbabel.FaberBabel
import com.fabernovel.faberbabel.internal.data.model.Config
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        wordingFromFaberbabel.text = this.getString(R.string.hello_world_title)
        wordingNotPresentInFaberbabel.text =
            resources.getString(R.string.wording_not_present_in_faberbabel)
        pluralFromFaberbabel.text = resources.getQuantityString(R.plurals.wording_plural_example, 0)

        wordingButton.setOnClickListener {
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }

    override fun attachBaseContext(newBase: Context) {
        // Must be injected as a Singleton by Dagger with application context
        val faberBabelSDK = FaberBabel(newBase)

        // Must be called on application start or when the language is updated
        // The Config object contains url of the faberbabel service, project id  and language code
        faberBabelSDK.asyncFetchFaberBabelWording(
            Config(
                "https://faberbabel-develop.herokuapp.com/translations/projects",
                "349a41ab-7815-4cf6-ae54-2e5f2304bfe9",
                Locale.getDefault().language
            )
        )


        super.attachBaseContext(faberBabelSDK.provideFaberBabelContextWrapper(newBase))
    }


}
