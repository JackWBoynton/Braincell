package net.bottomtextdanny.braincell.base;

public final class BCStringUtil {
    private static final String PARAM_TARGET = "{}";
    private static final StringBuilder BUILDER = new StringBuilder();

    public static String param(String msg, Object parameter) {
        free();
        BUILDER.append(msg);
        int start = -1;
        for (int i = 0; i < BUILDER.length(); i++) {
            if (start == -1) {
                if (BUILDER.charAt(i) == '{')
                    start = i;
            } else if (BUILDER.charAt(i) == '}') {
                    break;
            } else {
                start = -1;
            }
        }
        if (start != -1) {
            BUILDER.replace(start, start + 2, parameter.toString());
            return BUILDER.toString();
        }
        else return msg;
    }

    public static String params(String msg, Object... parameter) {
        free();
        BUILDER.append(msg);
        int[] indices = new int[parameter.length];
        int index = 0;
        int start = -1;
        for (int i = 0; i < BUILDER.length() && index < indices.length; i++) {
            if (start == -1) {
                if (BUILDER.charAt(i) == '{')
                    start = i;
            } else {
                if (BUILDER.charAt(i) == '}') {
                    indices[index] = i;
                    index++;
                }
                start = -1;
            }
        }
        int offset = 0;
        for (int i = 0; i < parameter.length; i++) {
            int lIndex = indices[i] - 1;
            if (lIndex != -1) {
                String p = parameter[i].toString();
                lIndex += offset;
                offset += p.length() - 2;
                BUILDER.replace(lIndex, lIndex + 2, p);
            }
        }
        return BUILDER.toString();
    }

    private static void free() {
        BUILDER.setLength(0);
        BUILDER.trimToSize();
    }

    private BCStringUtil() {}
}
