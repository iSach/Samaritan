package be.isach.samaritan.util;

import java.util.Random;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.util
 * Created by: Sacha
 * Created on: 17th May, 2016
 *             at 00:39 am
 */
public class MathUtils {

    private static Random random = new Random();

    public static Random getRandom() {
        return random;
    }

    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
