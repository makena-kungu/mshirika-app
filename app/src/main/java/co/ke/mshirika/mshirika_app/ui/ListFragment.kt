package co.ke.mshirika.mshirika_app.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import co.ke.mshirika.mshirika_app.ui.util.Transitions.itemToDetailReentry

abstract class ListFragment<B>(@LayoutRes contentLayoutId: Int) :
    MshirikaFragment<B>(contentLayoutId) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemToDetailReentry(view)
    }
}