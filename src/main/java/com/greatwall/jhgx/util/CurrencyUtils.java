package com.greatwall.jhgx.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 货币转换工具类
 * 
 * @author Sam
 */
public final class CurrencyUtils {

    /**
     * 小数点位数
     */
    private static final int SCALE = 2;

    private CurrencyUtils() {
        // 无参构造器
    }

    /**
     * 元转分
     * 
     * @param amount
     *            double类型的元
     * @return int类型的分
     */
    public static int parseCNY2Cent(double amount) {
        BigDecimal yuan = BigDecimal.valueOf(amount);
        BigDecimal cent = yuan.divide(BigDecimal.ONE, SCALE, RoundingMode.HALF_UP);
        return cent.multiply(new BigDecimal(100)).intValue();
    }

    /**
     * 分转元
     * 
     * @param amount
     *            int类型的分
     * @return double类型的元
     */
    public static double parseCent2CNY(int amount) {
        BigDecimal cent = new BigDecimal(amount);
        BigDecimal yuan = cent.divide(new BigDecimal(100), SCALE, RoundingMode.HALF_UP);
        return yuan.doubleValue();
    }

    /**
     * 计算总的金额
     * 
     * @param amounts
     *            金额列表
     * @return 总的金额
     */
    public static double sum(double... amounts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (double amount : amounts) {
            sum = sum.add(new BigDecimal(Double.toString(amount)));
        }
        return sum.divide(BigDecimal.ONE, SCALE, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 计算总的金额
     * 
     * @param amounts
     *            金额列表
     * @return 总的金额
     */
    public static double sumForStrings(String... amounts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (String amount : amounts) {
            String amountFee = StringUtils.trim(amount);
            if (NumberUtils.isNumber(amountFee)) {
                sum = sum.add(new BigDecimal(amountFee));
            }
        }
        return sum.divide(BigDecimal.ONE, SCALE, RoundingMode.HALF_UP).doubleValue();
    }
}
