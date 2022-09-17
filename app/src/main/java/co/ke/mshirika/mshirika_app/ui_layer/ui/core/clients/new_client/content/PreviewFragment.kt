package co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client.content

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentNewClientPreviewBinding
import co.ke.mshirika.mshirika_app.ui_layer.model_fragments.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.clients.new_client.CreateClientViewModel
import co.ke.mshirika.mshirika_app.ui_layer.ui.core.utils.create.ViewerFragment
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil.mediumDate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviewFragment : MshirikaFragment<FragmentNewClientPreviewBinding>(
    R.layout.fragment_new_client_preview
), ViewerFragment {

    private val viewModel: CreateClientViewModel by viewModels({ requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val columns = arrayOf(FIRST_COLUMN, SECOND_COLUMN)
        val data = mutableListOf(mutableMapOf<String, String>())

        viewModel.familyMembers.collectLatestLifecycle {
            val list = it.map { familyMember -> familyMember.name }
            binding.familyPreview.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                list
            )
        }

        Log.d(TAG, "onViewCreated: init")
        viewModel.generalData?.run {
            Log.d(TAG, "onViewCreated: general data not null")
            binding.name = firstName + middleName + lastName
            binding.memberNo = memNo
            //binding.address = it[CreateClientViewModel.KEY_ADDRESS] as Address

            data += mutableMapOf(
                FIRST_COLUMN to getString(R.string.date_of_birth),
                SECOND_COLUMN to dob.mediumDate
            )
            data += mutableMapOf(
                FIRST_COLUMN to "",
                SECOND_COLUMN to mobileNo.commonPrefixWith("+254")
            )
            data += mutableMapOf(
                FIRST_COLUMN to "",
                SECOND_COLUMN to if (gender) getString(R.string.male) else getString(R.string.female)
            )

            if (isStaff)
                data += mutableMapOf(
                    FIRST_COLUMN to "Is A Staff Member",
                    SECOND_COLUMN to getString(R.string.is_staff)
                )

            data += mutableMapOf(
                FIRST_COLUMN to getString(R.string.client_type),
                SECOND_COLUMN to clientType
            )
            data += mutableMapOf(
                FIRST_COLUMN to getString(R.string.client_classification),
                SECOND_COLUMN to clientClassification
            )
            data += mutableMapOf(
                FIRST_COLUMN to getString(R.string.submitted_on),
                SECOND_COLUMN to submitDate.mediumDate
            )
            data += mutableMapOf(
                FIRST_COLUMN to getString(R.string.national_id_no),
                SECOND_COLUMN to nationalId
            )
            data += mutableMapOf(
                FIRST_COLUMN to getString(R.string.email_address),
                SECOND_COLUMN to email
            )
            data += mutableMapOf(
                FIRST_COLUMN to getString(R.string.activation_date),
                SECOND_COLUMN to activationDate.mediumDate
            )
        }
        viewModel.data.collectLatestLifecycle {
            data.clear()


            binding.generalDataPreview.adapter = SimpleAdapter(
                requireContext(),
                data,
                android.R.layout.simple_list_item_activated_2,
                columns,
                intArrayOf(android.R.id.text1, android.R.id.text2)
            )
        }
    }

    override fun onNextPressed(): Boolean {
        viewModel.post()
        return true
    }

    companion object {
        private const val TAG = "PreviewFragment"

        const val FIRST_COLUMN = "1st_column"
        const val SECOND_COLUMN = "2nd_column"
    }
}