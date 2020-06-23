package com.fabernovel.faberbabel.sample

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fabernovel.faberbabel.appwording.FaberbabelSDK
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        wordingFromFaberbabel.text = this.getString(R.string.wording_from_faberbabel)
        wordingNotPresentInFaberbabel.text =
            resources.getString(R.string.wording_not_present_in_faberbabel)
        pluralFromFaberbabel.text = resources.getQuantityString(R.plurals.wording_plural_example, 0)
    }

    override fun attachBaseContext(newBase: Context?) {
        // Must be injected as a Singleton by Dagger
        val faberbabelSDK = FaberbabelSDK()

        super.attachBaseContext(faberbabelSDK.provideBabelContext(newBase))
    }
}
