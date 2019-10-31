package br.com.oversight.sefisca.util;

import javax.xml.bind.DatatypeConverter;

public class StringXorCriptografia {

    private static final String CHAVE = "9AX834*48605cU";

    public String encode(String s, String key) {
        return base64Encode(xorWithKey(s.getBytes(), key.getBytes()));
    }

    public String decode(String s, String key) {
        return new String(xorWithKey(base64Decode(s), key.getBytes()));
    }

    private static byte[] xorWithKey(byte[] a, byte[] key) {
        byte[] out = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = (byte) (a[i] ^ key[i % key.length]);
        }
        return out;
    }

    private static byte[] base64Decode(String s) {
        try {
            return DatatypeConverter.parseBase64Binary(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String base64Encode(byte[] bytes) {
        String encoded = DatatypeConverter.printBase64Binary(bytes);
        return encoded.replaceAll("\\s", "");
    }

    public static String getChave() {
        return CHAVE;
    }

}
