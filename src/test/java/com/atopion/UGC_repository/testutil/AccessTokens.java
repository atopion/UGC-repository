package com.atopion.UGC_repository.testutil;

import com.atopion.UGC_repository.security.TokenGenerator;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class AccessTokens {

    public static MockHttpServletRequestBuilder withValidHeaders(MockHttpServletRequestBuilder action) {
        String key = "7E0A55872199849864FBA3D029B2A4FC519818AA21DBB23D01FD57A1F917C12C2E1A0F026072C5B7BF3660A73A319F7D7CAD2725B57BFAA7A3919FD739018049";
        String secret = "0649A13A3CBFA10E9A42F60A212962DC8F43E481B26C78F2CE6686E4059A5ED8D5D7CD3B6F40FDF917C95550FABC75CCA3F35FC24DAEDF26DB1C5C4EA3D0B784";

        String accessToken = TokenGenerator.generateTOTP(secret, TokenGenerator.getCurrentStep());

        return action
                        .header("X-Auth-Access-Id", "1")
                        .header("X-Auth-Access-Key", key)
                        .header("X-Auth-Access-Token", accessToken);
    }

    public static MockHttpServletRequestBuilder withInvalidHeaders(MockHttpServletRequestBuilder action) {
        String key = "286E4832210C5195BB95342E1B5B1AB490B5B073B08153D1550B7D687D31F13A9EE2639797B3B578B69CAC7AACD4E3DA49A8C4DC58BBF1BB0608ADEACED91782";
        String secret = "84E647F67C9C18DB28E66020D95F1758B45F05B6B65E0B6286409CA7617C8A2CB3BD0BCAFB72ECB55C6DD690DB8BF8DA20A048953931EE30B055665BEBCF177D";

        String accessToken = TokenGenerator.generateTOTP(secret, TokenGenerator.getCurrentStep());

        return action
                .header("X-Auth-Access-Id", "1")
                .header("X-Auth-Access-Key", key)
                .header("X-Auth-Access-Token", accessToken);
    }
}
