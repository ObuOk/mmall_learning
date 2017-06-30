package com.mmall.util;

import java.math.BigDecimal;

/**
 * Created by guojianzhong on 2017/6/29.
 */
public class BigDecimalUtil {

    public static BigDecimal add(double a, double b) {
       return new BigDecimal(a + "").add(new BigDecimal(b + ""));
    }

    public static BigDecimal mul(double a, double b) {
        return new BigDecimal(a + "").multiply(new BigDecimal(b + ""));
    }
}


