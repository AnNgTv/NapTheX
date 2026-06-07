package net.napthex.utils;

import java.util.Random;

public class FNum {
    public static int randomInt(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    public static int ri(String s) {
        return Integer.valueOf(s);
    }

    public static double rd(String s) {
        return Double.valueOf(s);
    }
}
