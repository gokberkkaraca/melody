package ch.epfl.sweng.melody;

/**
 * Created by yusiz on 2017/11/22.
 */

public class Trying {
    public static void main(String[] args) {
        String text = "trying to delete the rest of text";

        System.out.print(takeSubtext(text, 20));

    }
    static private String takeSubtext(String text, int length) {
        if (text.length() > length) {
            String reverse = new StringBuffer(text.substring(0, length)).reverse().toString();
            int i = 0;
            while (i < length && reverse.charAt(0) != ' ') {
                reverse = reverse.substring(1, reverse.length());
                i++;
            }
            return new StringBuffer(reverse).reverse().toString();
        } else {
            return text;
        }
    }
}
