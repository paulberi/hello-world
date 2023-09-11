package se.metria.markkoll.util;

public class StringUtil {
    public static String capitalize(String s) {
        if (io.netty.util.internal.StringUtil.isNullOrEmpty(s)) {
            return s;
        }
        else {
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        }
    }
}
