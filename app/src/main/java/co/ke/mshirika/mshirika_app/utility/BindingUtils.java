package co.ke.mshirika.mshirika_app.utility;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.List;

import co.ke.mshirika.mshirika_app.ui.util.ViewUtils;

public class BindingUtils {

    @NonNull
    public static String mediumDate(List<Integer> date) {
        return ViewUtils.INSTANCE.mediumDate(date);
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
}
