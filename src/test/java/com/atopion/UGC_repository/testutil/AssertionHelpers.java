package com.atopion.UGC_repository.testutil;

import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class AssertionHelpers {

    public static void assertResponseContainsText(MockHttpServletResponse response, String text)
            throws AssertionError, UnsupportedEncodingException {

        String tmp = response.getContentAsString(StandardCharsets.UTF_8);

        // WORKAROUND: Somehow the whole string will be deleted when using tmp.replace("\n", "").
        // This acts as a workaround where this behaviour does not happen.
        StringBuilder sb = new StringBuilder();
        tmp.chars().forEach(c -> sb.append((char)(c == 10 || c == 13 ? ' ' : c)));
        String respStr = sb.toString();

        respStr = respStr.replace("\t", "");
        respStr = respStr.replace(" ", "");

        text = text
                .replace("\n", "")
                .replace("\t", "")
                .replace(" ", "")
                .replace("	", "");

        assertThat("Could not find expected text in the reponse. ", respStr, containsString(text));
    }

}
