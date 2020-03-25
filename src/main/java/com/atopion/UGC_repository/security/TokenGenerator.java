package com.atopion.UGC_repository.security;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.Date;

/**
 * Generates TOTP tokens for the API. Implementation based on the reference implementation
 * found in RFC6238. Resulting tokens are Base64 Encoded 64-byte hashes.
 */
public class TokenGenerator {

    private static byte[] hmac_sha(byte[] keyBytes, byte[] text) {
        try {
            Mac hmac;
            hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException ex) {
            throw new UndeclaredThrowableException(ex);
        }
    }

    public static byte[] hexStr2Bytes(String hex) {
        byte[] bArray = new BigInteger("10" + hex,16).toByteArray();

        byte[] ret = new byte[bArray.length - 1];
        System.arraycopy(bArray, 1, ret, 0, ret.length);
        return ret;
    }

    public static String[] getTimeSteps() {
        long T0 = 0;
        long X  = 30;

        Date date1 = new Date();
        Date date2 = new Date();
        date1.setTime(date1.getTime() - 30000L);

        long time1 = date1.getTime() / 1000L, time2 = date2.getTime() / 1000L;

        String steps1 = Long.toHexString((time1 - T0)/X).toUpperCase(), steps2 = Long.toHexString((time2 - T0)/X).toUpperCase();

        while (steps1.length() < 16) steps1 = "0" + steps1;
        while (steps2.length() < 16) steps2 = "0" + steps2;

        return new String[] {steps1, steps2};
    }

    public static String getCurrentStep() {
        long T0 = 0;
        long X  = 30;

        long time = new Date().getTime() / 1000L;

        String step = Long.toHexString((time - T0)/X).toUpperCase();
        while (step.length() < 16) step = "0" + step;

        return step;
    }

    public static String generateTOTP(String key, String time) throws IllegalArgumentException {

        // Get the HEX in a Byte[]
        //byte[] msg = hexStr2Bytes(time);
        //byte[] k = hexStr2Bytes(key);
        try {
            // Get the HEX in byte arrays
            byte[] msg = Hex.decodeHex(time);
            byte[] k = Hex.decodeHex(key);

            // Generate
            byte[] hash = hmac_sha(k, msg);

            return Hex.encodeHexString(hash, false);

        } catch (DecoderException ex) {
            throw new IllegalArgumentException("Unsupported key.");
        }
    }
}
