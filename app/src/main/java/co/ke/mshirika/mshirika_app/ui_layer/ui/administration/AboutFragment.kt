package co.ke.mshirika.mshirika_app.ui_layer.ui.administration

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.oss.licenses.OssLicensesActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutFragment:Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TODO("Create the required view")
    }

    fun onLicenses() {
        startActivity(Intent(requireActivity(),OssLicensesActivity::class.java))
    }
}