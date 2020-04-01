package com.atopion.UGC_repository.security;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class DemoUserProvider {

    public final static DemoUserProvider instance = new DemoUserProvider();

    private final String secret =
            "BC8C2C18AAD995082C51620BB91C3E6249242534E8D19B4BAFFD225E988AA2396866E90F6C4BDCC3CEACC4A008565D0709E374B3F2D60643DAF73151A38ABFC2";

    private final String key =
            "6F033DD625214E4F76AF245C83A32705459D0C435FA8F793259ECCA3809A8E4784DBF4FF5DFA4893174AA8A64624FF32E76E40FD2A8772C5B9EE7462BBE659B7";

    private final String salt;

    private final String keyhash;


    private DemoUserProvider() {
        String keyhash1;
        String salt1;

        try {
            salt1 = BCrypt.gensalt(8);
            keyhash1 = BCrypt.hashpw(Hex.decodeHex(key), salt1);
        } catch (DecoderException ex) {
            ex.printStackTrace();
            salt1 = null;
            keyhash1 = null;
        }
        this.keyhash = keyhash1;
        this.salt = salt1;
    }

    public boolean verifyPassword(String password) {
        try {
            return BCrypt.checkpw(Hex.decodeHex(password), keyhash);
        } catch (Exception e) {
            return false;
        }
    }


    public String getSecret() {
        try {
            byte[] secArr = Hex.decodeHex(secret);
            byte[] keyArr = Hex.decodeHex(key);

            byte[] result = new byte[64];

            for(int i = 0; i < secArr.length; i++) {
                result[i] = (byte) (secArr[i] ^ keyArr[i]);
            }

            return Hex.encodeHexString(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getKey() {
        return key;
    }

    public String getKeySalt() {
        return salt;
    }
}
