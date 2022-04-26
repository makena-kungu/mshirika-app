package co.ke.mshirika.mshirika_app.utility;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.List;

import co.ke.mshirika.mshirika_app.ui_layer.ui.util.DateUtil;
import co.ke.mshirika.mshirika_app.ui_layer.ui.util.ViewUtils;

public class BindingUtils {

    @NonNull
    public static String mediumDate(List<Integer> date) {
        return DateUtil.INSTANCE.mediumDate(date);
    }

    @NonNull
    public static String amount(double amount) {
        return ViewUtils.INSTANCE.getAmt(amount);
    }

    @NonNull
    public static String amount(int amount) {
        return amount((double) amount);
    }

    @NonNull
    public static String amount(BigDecimal amount) {
        return ViewUtils.INSTANCE.getAmt(amount);
    }

/*
    @BindingAdapter({"android:onclick"})
    public static void setOnClick(@NonNull View view, View.OnClickListener listener) {
        view.setOnClickListener(listener);
    }
*/
}
