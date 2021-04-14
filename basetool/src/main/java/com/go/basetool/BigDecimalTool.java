package com.go.basetool;

import java.math.BigDecimal;

public class BigDecimalTool {

    public static BigDecimal setScaleZeroOrNull(BigDecimal b) {
        if (null == b) {
            b = new BigDecimal(0);
        }

        if (b.compareTo(new BigDecimal(0)) == 0) {
            b = b.setScale(4, BigDecimal.ROUND_UP);
        }
        return b;
    }

}
