package be.isach.samaritan.pokemongo;

import java.util.HashMap;
import java.util.Map;

/**
 * Package: be.isach.samaritan.pokemongo
 * Created by: sachalewin
 * Date: 27/07/16
 * Project: samaritan
 */
public class LevelRegistry {

    /**
     * key: Level.
     * value: Exp required for key + 1.
     */
    private static Map<Integer, Integer> EXP_NEEDED;

    public static Integer getNextLevelExp(int i) {
        if(EXP_NEEDED == null) {
            EXP_NEEDED = new HashMap<>();
            EXP_NEEDED.put(1, 1000);
            EXP_NEEDED.put(2, 2000);
            EXP_NEEDED.put(3, 3000);
            EXP_NEEDED.put(4, 4000);
            EXP_NEEDED.put(5, 5000);
            EXP_NEEDED.put(6, 6000);
            EXP_NEEDED.put(7, 7000);
            EXP_NEEDED.put(8, 8000);
            EXP_NEEDED.put(9, 9000);
            EXP_NEEDED.put(10, 10000);
            EXP_NEEDED.put(11, 10000);
            EXP_NEEDED.put(12, 10000);
            EXP_NEEDED.put(13, 10000);
            EXP_NEEDED.put(14, 15000);
            EXP_NEEDED.put(15, 20000);
            EXP_NEEDED.put(16, 20000);
            EXP_NEEDED.put(17, 20000);
            EXP_NEEDED.put(18, 25000);
            EXP_NEEDED.put(19, 25000);
            EXP_NEEDED.put(20, 50000);
            EXP_NEEDED.put(21, 75000);
            EXP_NEEDED.put(22, 100000);
            EXP_NEEDED.put(23, 125000);
            EXP_NEEDED.put(24, 150000);
            EXP_NEEDED.put(25, 190000);
            EXP_NEEDED.put(26, 200000);
            EXP_NEEDED.put(27, 250000);
            EXP_NEEDED.put(28, 300000);
            EXP_NEEDED.put(29, 350000);
            EXP_NEEDED.put(30, 500000);
            EXP_NEEDED.put(31, 500000);
            EXP_NEEDED.put(32, 750000);
            EXP_NEEDED.put(33, 1000000);
            EXP_NEEDED.put(34, 1250000);
            EXP_NEEDED.put(35, 1500000);
            EXP_NEEDED.put(36, 2000000);
            EXP_NEEDED.put(37, 2500000);
            EXP_NEEDED.put(38, 3000000);
            EXP_NEEDED.put(39, 5000000);
        }
        try {
            return EXP_NEEDED.get(i);
        } catch (Exception exc) {
            return -1;
        }
    }

}
