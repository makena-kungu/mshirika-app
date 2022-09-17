package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.core.client


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SmsCampaign(
    val totalFilteredRecords: Int,
    @SerializedName("pageItems")
    val smses: List<MshirikaSms>
) {
    @Keep
    data class MshirikaSms(
        val id: Int,
        val campaignName: String,
        val campaignType: CampaignType,
        val runReportId: Int,
        val reportName: String,
        val paramValue: String,
        val campaignStatus: CampaignStatus,
        val triggerType: TriggerType,
        val campaignMessage: String,
        val smsCampaignTimeLine: SmsCampaignTimeLine,
        val recurrenceStartDate: Long,
        val providerId: Int,
        val isNotification: Boolean,
        val nextTriggerDate: Long,
        val lastTriggerDate: List<Int>,
        val recurrence: String
    ) {
        @Keep
        data class CampaignType(
            val id: Int,
            val code: String,
            val value: String
        )

        @Keep
        data class CampaignStatus(
            val id: Int,
            val code: String,
            val value: String
        )

        @Keep
        data class TriggerType(
            val id: Int,
            val code: String,
            val value: String
        )

        @Keep
        data class SmsCampaignTimeLine(
            val submittedOnDate: List<Int>,
            val submittedByUsername: String,
            val activatedOnDate: List<Int>,
            val activatedByUsername: String,
            val closedOnDate: List<Int>,
            val closedByUsername: String
        )
    }
}