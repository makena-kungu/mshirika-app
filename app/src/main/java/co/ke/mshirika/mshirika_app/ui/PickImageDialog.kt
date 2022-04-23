package co.ke.mshirika.mshirika_app.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.BottomSheetPickImageBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class PickImageDialog(context: Context, listener: OnImageSelectedListener) : BottomSheetDialog(context) {
    lateinit var binding: BottomSheetPickImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BottomSheetPickImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list = listOf(
            R.string.choose_image to R.drawable.ic_round_insert_photo_24,
            R.string.capture_image to R.drawable.ic_round_photo_camera_24
        ).map {
            context.getString(it.first) to it.second
        }

        binding.menuList.adapter = MshirikaMenuAdapter(
            context,
            list
        )
        binding.menuList.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> {
                    //choose image from the library
                }
                else -> {
                    //capture an image
                }
            }
        }
    }
}

class MshirikaMenuAdapter(context: Context, objects: List<Pair<String, Int>>) :
    ArrayAdapter<Pair<String, Int>>(
        context,
        android.R.layout.simple_expandable_list_item_1,
        objects
    ) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_expandable_list_item_1, parent, false)

        val textView: TextView = view.findViewById(android.R.id.text1)
        getItem(position)?.apply {
            textView.text = first
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(second, 0, 0, 0)
        }

        return view
    }
}

interface OnImageSelectedListener {
    fun onImageSelected(path: String)
}