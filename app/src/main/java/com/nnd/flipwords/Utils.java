package com.nnd.flipwords;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 * Created by Android dev on 7/24/17.
 */

public class Utils {
    public static String getRandomDate() {
        GregorianCalendar gc = new GregorianCalendar();

        int year = randBetween(2010, 2017);
        int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));

        gc.set(gc.YEAR, year);
        gc.set(gc.DAY_OF_YEAR, dayOfYear);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(gc.getTime());
    }

    public static int randBetween(int min, int max) {
        Random r = new Random();

        return r.nextInt(max - min) + min;
    }
}
