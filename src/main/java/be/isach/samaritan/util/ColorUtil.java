package be.isach.samaritan.util;

import java.awt.*;
import java.util.Random;

/**
 * Created by sacha on 18/12/16.
 */
public class ColorUtil {

    public static Color getRandomColor() {
        Random random = MathUtils.getRandom();
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }


}
