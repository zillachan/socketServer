package com.zillatimes.demo.socket;

public class Utils {
    private final static char[] CHARS = "0123456789ABCDEF".toCharArray();

    public static String byte2HexStr(byte[] b, int iLen) {
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < iLen; n++) {
            sb.append(CHARS[(b[n] & 0xFF) >> 4]);
            sb.append(CHARS[b[n] & 0x0F]);
            sb.append(' ');
        }
        return sb.toString();
    }

    public static byte[] hexStr2Bytes(String src) {
        src = src.trim().replaceAll(" ", "").toUpperCase();
        int m = 0, n = 0;
        int iLen = src.length() / 2;
        byte[] ret = new byte[iLen];

        for (int i = 0; i < iLen; i++) {
            m = i * 2 + 1;
            n = m + 1;
            ret[i] = (byte) (Integer.decode("0x" + src.substring(i * 2, m) + src.substring(m, n)) & 0xFF);
        }
        return ret;
    }

}