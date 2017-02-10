package bemobi.hire.me.api.util;

/**
 * Created by rrodovalho on 09/02/17.
 */

//Get Base62 from https://gist.github.com/subchen/11200812

public class BaseEncodeUtil {

    private static final char[] digitsChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final int BASE = digitsChar.length;
    private static final int FAST_SIZE = 'z';
    private static final int[] digitsIndex = new int[FAST_SIZE + 1];


    static {
        for (int i = 0; i < FAST_SIZE; i++) {
            digitsIndex[i] = -1;
        }
        for (int i = 0; i < BASE; i++) {
            digitsIndex[digitsChar[i]] = i;
        }
    }

    public static String encodeToBase62(long number) {
        if (number < 0) throw new IllegalArgumentException("Number(Base62) must be positive: " + number);
        if (number == 0) return "0";
        StringBuilder buf = new StringBuilder();
        while (number != 0) {
            buf.append(digitsChar[(int) (number % BASE)]);
            number /= BASE;
        }
        return buf.reverse().toString();
    }

}
