package bean;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Hasher {
    private static final String ALGORITHM_NAME = "SHA";

    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM_NAME);
        byte[] hash = md.digest(password.getBytes());

        return hexToString(hash);
    }

    public static String hexToString(byte[] bytes) {
        StringBuffer buff = new StringBuffer();
        for (int i=0; i<bytes.length; i++) {
            int val = bytes[i];
            val = val & 0xff;  // remove higher bits, sign
            if (val<16) buff.append('0'); // leading 0
            buff.append(Integer.toString(val, 16));
        }
        return buff.toString();
    }
}