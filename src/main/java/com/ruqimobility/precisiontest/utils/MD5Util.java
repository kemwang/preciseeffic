package com.ruqimobility.precisiontest.utils;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * TODO
 *
 * @author liujia
 * @date 3/23 11:07
 */

public class MD5Util {

    public static String generateMD5(String baseCommit, String compareCommit) {
        String combinedString = baseCommit + compareCommit;
        String md5Hash = null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(combinedString.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            md5Hash = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return md5Hash;
    }

    public static String md5(String str) {
        byte[] code = null;
        try {
            code = MessageDigest.getInstance("md5").digest(str.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        BigInteger bi = new BigInteger(code);
        return bi.abs().toString(32).toLowerCase();
    }
}

