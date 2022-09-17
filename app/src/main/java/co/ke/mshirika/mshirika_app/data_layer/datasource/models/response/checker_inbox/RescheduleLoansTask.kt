package co.ke.mshirika.mshirika_app.data_layer.datasource.models.response.checker_inbox

import com.google.gson.annotations.SerializedName

data class RescheduleLoansTask(
    @SerializedName("id") var id: Int,
    @SerializedName("clientName") var clientName: String,
    @SerializedName("loanAccountNumber") var loanAccountNo: String,
    @SerializedName("rescheduleFromDate") var rescheduleFromDate: Array<Int>,
    @SerializedName("actionName") var action: String,
    @SerializedName("rescheduleReasonCodeValue")
    var rescheduleReasonCodeValue: RescheduleReasonCodeValue
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RescheduleLoansTask

        if (id != other.id) return false
        if (clientName != other.clientName) return false
        if (loanAccountNo != other.loanAccountNo) return false
        if (!rescheduleFromDate.contentEquals(other.rescheduleFromDate)) return false
        if (action != other.action) return false
        if (rescheduleReasonCodeValue != other.rescheduleReasonCodeValue) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + clientName.hashCode()
        result = 31 * result + loanAccountNo.hashCode()
        result = 31 * result + rescheduleFromDate.contentHashCode()
        result = 31 * result + action.hashCode()
        result = 31 * result + rescheduleReasonCodeValue.hashCode()
        return result
    }
}