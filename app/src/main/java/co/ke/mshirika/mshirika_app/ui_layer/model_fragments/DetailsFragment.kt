package co.ke.mshirika.mshirika_app.ui_layer.model_fragments

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.Transitions.itemToDetailSharedElementEnterTransition

abstract class DetailsFragment<B>(@LayoutRes contentLayoutId: Int) :
    MshirikaFragment<B>(contentLayoutId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemToDetailSharedElementEnterTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}