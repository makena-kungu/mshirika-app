package co.ke.mshirika.mshirika_app.ui.create.new_client

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import androidx.fragment.app.viewModels
import co.ke.mshirika.mshirika_app.R
import co.ke.mshirika.mshirika_app.databinding.FragmentNewClientPreviewBinding
import co.ke.mshirika.mshirika_app.ui.MshirikaFragment
import co.ke.mshirika.mshirika_app.ui.create.ViewerFragment
import co.ke.mshirika.mshirika_app.ui.util.DateUtil.mediumDate
import kotlinx.coroutines.flow.collectLatest

class PreviewFragment :
    MshirikaFragment<FragmentNewClientPreviewBinding>(R.layout.fragment_new_client_preview),
    ViewerFragment {

    private val viewModel: ViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val columns = arrayOf(FIRST_COLUMN, SECOND_COLUMN)
        val data = mutableListOf(mutableMapOf<String, String>())
        lifecycleScope.launchWhenCreated {
            viewModel.data.collectLatest {
                data.clear()
                with(it[ViewModel.KEY_GENERAL_DATA] as GeneralData) {
                    binding.name = firstName + middleName + lastName
                    binding.memberNo = memNo
                    binding.address = it[ViewModel.KEY_ADDRESS] as Address

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

                binding.generalDataPreview.adapter = SimpleAdapter(
                    requireContext(),
                    data,
                    android.R.layout.simple_list_item_activated_2,
                    columns,
                    intArrayOf(android.R.id.text1, android.R.id.text2)
                )
            }

            viewModel.familyMembers.collectLatest {
                val list = it.map { familyMember -> familyMember.name }
                binding.familyPreview.adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    list
                )
            }
        }

    }

    override fun onNextPressed() {
        viewModel.post()
    }

    companion object {
        const val FIRST_COLUMN = "1st_column"
        const val SECOND_COLUMN = "2nd_column"
    }
}