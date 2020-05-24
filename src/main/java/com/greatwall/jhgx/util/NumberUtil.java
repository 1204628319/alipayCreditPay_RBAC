package com.greatwall.jhgx.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class NumberUtil {

    private static final Pattern INTEGER_PATTERN = Pattern.compile("^[-\\+]?[\\d]*$");

    private static final Pattern NUM_PATTERN = Pattern.compile("^[-\\+]?[.\\d]*$");
    private static final String ZERO_STR = "0.00";

    /**
     * 分转元，保留两位小数
     */
    public static String changeF2Y(Long amt) {
        if(amt == null || amt == 0L) {
            return ZERO_STR;
        }
        BigDecimal money = BigDecimal.valueOf(amt).divide(new BigDecimal(100));
        DecimalFormat decimalFormat = new DecimalFormat(ZERO_STR);
        return decimalFormat.format(money);
    }

    /**
     * 提供精确的小数位四舍五入处理
     * @param num 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @author zsd
     * @date 2020/3/23 15:48
     **/
    public static double round(double num, int scale) {
        if (scale < 0) {
            scale = -scale;
        }
        BigDecimal bigDecimal = BigDecimal.valueOf(num);
        BigDecimal numOne = new BigDecimal("1");
        return bigDecimal.divide(numOne, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String amtConvert(String amt) {
        if(StringUtils.isBlank(amt)) {
            return null;
        }

        // 分转元, 保留两位小数
        return new BigDecimal(amt).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
    }
}
