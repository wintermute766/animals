package ru.sberbank.mobile.common.rate;

import java.math.BigDecimal;

import ru.sberbank.mobile.common.rate.entity.Rate;
import ru.sberbank.mobile.core.format.DecimalFormatter;

/**
 * @author QuickNick
 */
public class ConverterUtils {

    public static BigDecimal convert(BigDecimal sourceAmount, Rate sourceRate, Rate targetRate) {
        BigDecimal roubleAmount = sourceAmount.multiply(sourceRate.getScaledRelation());
        BigDecimal targetAmount = roubleAmount.divide(targetRate.getScaledRelation(),
                DecimalFormatter.DEFAULT_SCALE, BigDecimal.ROUND_HALF_UP);
        return targetAmount;
    }
}
