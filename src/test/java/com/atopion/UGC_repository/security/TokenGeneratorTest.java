package com.atopion.UGC_repository.security;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-integrationtests.properties")
class TokenGeneratorTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testOTP() throws DecoderException {
        String text = "A80339BD3AFC3E539D559483513387671EB4FCC992F59F7D966C9E016DAB084A2F320BBAD3447E77FE8AF57E45296248764A550B7DCBA6234BD2D0875F5C7417";
        String key  = "3B14E4C072D37C0952043E7A5275B951C687EE6311A03BBAB788A0C9ACC36A0E47BD523594A41AE1E5A5C10762D6EC5538522F1E80B36E58BCE8A243EA3D0049";

        /*byte[] keyarr  = new BigInteger(10 + key,  16).toByteArray();
        byte[] textarr = new BigInteger(10 + text, 16).toByteArray();*/
        byte[] keyarr  = Hex.decodeHex(key);
        byte[] textarr = Hex.decodeHex(text);
        byte[] cipher  = new byte[keyarr.length];
        byte[] result  = new byte[keyarr.length];

        for(int i = 0; i < keyarr.length; i++)
            cipher[i] = (byte) (textarr[i] ^ keyarr[i]);

        System.out.println("CIPHER: " + Arrays.toString(cipher));

        for(int i = 0; i < keyarr.length; i++)
            result[i] = (byte) (cipher[i] ^ keyarr[i]);

        System.out.println("TEXT: " + Arrays.toString(result));
        System.out.println("EQUAL: " + Arrays.equals(result, textarr));

        byte[] test_1_1 = TokenGenerator.hexStr2Bytes(key);
        byte[] test_1_2 = Hex.decodeHex(key);

        System.out.println("TEST 1: " + Arrays.equals(test_1_1, test_1_2));
        System.out.println("TEST 1: " + (test_1_1.length == test_1_2.length));

    }

    @Test
    void testMasterKey() throws Exception {
        String text   = "A80339BD3AFC3E539D559483513387671EB4FCC992F59F7D966C9E016DAB084A2F320BBAD3447E77FE8AF57E45296248764A550B7DCBA6234BD2D0875F5C7417";
        String key    = "3B14E4C072D37C0952043E7A5275B951C687EE6311A03BBAB788A0C9ACC36A0E47BD523594A41AE1E5A5C10762D6EC5538522F1E80B36E58BCE8A243EA3D0049";
        String master = "898BAFF0B52F5D02DADB9191676FA3BD22929845EAC7ED29940CBBFD20E0D2FE0E8F25FD7397C399415D2A222CE522C29BD5F335FEDCEB19DFC865A6DEF010D7";

        byte[] _text   = Hex.decodeHex(text);
        byte[] _key    = Hex.decodeHex(key);
        byte[] _master = Hex.decodeHex(master);

        byte[] _ciph1  = new byte[64];
        byte[] _ciph2  = new byte[64];

        for(int i = 0; i < _text.length; i++) {
            _ciph1[i] = (byte) (_text[i]  ^ _key[i]);
            _ciph2[i] = (byte) (_ciph1[i] ^ _master[i]);
        }

        byte[] _res1   = new byte[64];
        byte[] _res2   = new byte[64];

        for(int i = 0; i < _text.length; i++) {
            _res1[i] = (byte) (_ciph2[i] ^ _master[i]);
            _res2[i] = (byte) (_res1[i]  ^ _key[i]);
        }

        System.out.println("TEXT: " + Hex.encodeHexString(_text));
        System.out.println("RES:  " + Hex.encodeHexString(_res2));
        assertArrayEquals(_text, _res2);
    }

    @Test
    void testAccess() throws Exception {

        String key = "7E0A55872199849864FBA3D029B2A4FC519818AA21DBB23D01FD57A1F917C12C2E1A0F026072C5B7BF3660A73A319F7D7CAD2725B57BFAA7A3919FD739018049";
        String secret = "0649A13A3CBFA10E9A42F60A212962DC8F43E481B26C78F2CE6686E4059A5ED8D5D7CD3B6F40FDF917C95550FABC75CCA3F35FC24DAEDF26DB1C5C4EA3D0B784";

        String accessToken = TokenGenerator.generateTOTP(secret, TokenGenerator.getCurrentStep());

        MvcResult result = mvc.perform(get("/rest/applications?format=json")
                .header("X-Auth-Access-Id", "1")
                .header("X-Auth-Access-Key", key)
                .header("X-Auth-Access-Token", accessToken)
        ).andReturn();

        assertEquals("Did not get status 200.", 200, result.getResponse().getStatus());
        System.out.println(result.getResponse().getContentAsString());
    }

    int fkt(Integer i) {
        if(i != null)
            return i;
        else
            return -1;
    }

    @Test
    void tryout() {
        System.out.println(fkt(1) + " ->  " + fkt(null));

    }

    @Test
    void verifyPassword() {
        String key  = "6F033DD625214E4F76AF245C83A32705459D0C435FA8F793259ECCA3809A8E4784DBF4FF5DFA4893174AA8A64624FF32E76E40FD2A8772C5B9EE7462BBE659B7";
        String key2 = "D5B27EBDB65792D4322EBAEE901730BBA90E2AF831C85F5C2BB929435956F4E9C6ED2B5B87F790F0308BA8961DD98D6E1BC86E345B0E9688ABF01101A94470A9";

        /*DemoUserProvider demoUser = DemoUserProvider.instance;

        System.out.println(demoUser.verifyPassword(key));
        System.out.println(demoUser.verifyPassword(key2));*/
    }

    @Test
    void testSalt() throws Exception {
        for(int i = 0; i < 10; i++)
            System.out.println(BCrypt.gensalt(8));

        String salt = "D5B27EBDB65792D4322EBAEE901730BBA90E2AF831C85F5C2BB929435956F4E9C6ED2B5B87F790F0308BA8961DD98D6E1BC86E345B0E9688ABF01101A94470A9";
        String bcrypt_salt = "$2a$08$" + Base64.encodeBase64String(Hex.decodeHex(salt));
        String pw   = "mypassword";

        System.out.println("SALT: " + bcrypt_salt);
        System.out.println("\n" + BCrypt.hashpw(pw, bcrypt_salt));
    }


    @Test
    void completeTest() {
        // Find User.
        //DemoUserProvider demoUser = DemoUserProvider.instance;

        // Given information:
        /*String key =
                "6F033DD625214E4F76AF245C83A32705459D0C435FA8F793259ECCA3809A8E4784DBF4FF5DFA4893174AA8A64624FF32E76E40FD2A8772C5B9EE7462BBE659B7";

        String accessToken = TokenGenerator.generateTOTP(demoUser.getSecret(), TokenGenerator.getCurrentStep());


        // Step 1: Verify the given password against a (hashed and salted) stored password.
        assertTrue("Test 1: Key wrong: ", demoUser.verifyPassword(key));

        // Step 2: Verify the TOTP token.
        // Step 2.1: Decrypt the stored secret with the given key.
        String secret = demoUser.getSecret();

        // Step 2.2: Verify given access token with secret.
        boolean found = false;
        for(String step : TokenGenerator.getTimeSteps())
            if(TokenGenerator.generateTOTP(secret, step).equals(accessToken))
                found = true;

        assertTrue("Test 1: Token wrong: ", found);*/
    }


    @Test
    void generateTOTP() {
        // Seed for HMAC-SHA1 - 20 bytes
        /*String seed = "3132333435363738393031323334353637383930";
        // Seed for HMAC-SHA256 - 32 bytes
        String seed32 = "3132333435363738393031323334353637383930" +
                "313233343536373839303132";
        // Seed for HMAC-SHA512 - 64 bytes
        String seed64 = "3132333435363738393031323334353637383930" +
                "3132333435363738393031323334353637383930" +
                "3132333435363738393031323334353637383930" +
                "31323334";*/
        String seed64 = "025C03708FC041C6A65F099B8FB3AC67" +
                        "A8E32EAAE8D833BAB8B67374A69A4200" +
                        "6930D926186597CAA4687A0A36D1AB70" +
                        "078488B11F6CEC4C2AA22F49D4AA6560";
        System.out.println("Length: " + seed64.length());

        long T0 = 0;
        long X = 30;
        Date date1 = new Date();
        Date date2 = new Date();
        date2.setTime(date2.getTime() + 30000L);

        long[] testTime = {59L, 1111111109L, 1111111111L,
                1234567890L, 2000000000L, 20000000000L, date1.getTime() / 1000L, date2.getTime() / 1000L};

        String steps = "0";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            System.out.println(
                    "+---------------+-----------------------+" +
                            "------------------+----------------------------------------------------------------------------------------+--------+");
            System.out.println(
                    "|  Time(sec)    |   Time (UTC format)   " +
                            "| Value of T(Hex)  |                                      TOTP                                              | Mode   |");
            System.out.println(
                    "+---------------+-----------------------+" +
                            "------------------+----------------------------------------------------------------------------------------+--------+");

            for (int i=0; i<testTime.length; i++) {
                long T = (testTime[i] - T0)/X;
                steps = Long.toHexString(T).toUpperCase();
                while (steps.length() < 16) steps = "0" + steps;
                String fmtTime = String.format("%1$-11s", testTime[i]);
                String utcTime = df.format(new Date(testTime[i]*1000));

                System.out.print("|  " + fmtTime + "  |  " + utcTime + "  | " + steps + " |");
                System.out.println(TokenGenerator.generateTOTP(seed64, steps) + "| SHA512 |");

                System.out.println(
                        "+---------------+-----------------------+" +
                                "------------------+----------------------------------------------------------------------------------------+--------+");
            }
        } catch (final Exception e){
            System.out.println("Error : " + e);
        }
    }
}