package com.kernelapps.emicalc;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class PrasingTheDouble {
    double aDouble;

    public PrasingTheDouble(double mDouble) {
        this.aDouble = mDouble;
    }

    public String getaDouble() {
        NumberFormat anotherFormat = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat df = (DecimalFormat) anotherFormat;
        return df.format(this.aDouble);
    }
}
