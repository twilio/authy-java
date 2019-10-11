package com.authy.api;

import com.authy.AuthyApiClient;
import com.authy.AuthyException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.net.www.protocol.http.HttpURLConnection;

import java.util.HashMap;
import java.util.Map;

import static com.authy.api.Error.Code.TOKEN_INVALID;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class TokensTest extends TestApiBase {

    private Tokens tokens;
    private int testUserId = 123456;
    private String testToken = "123456";

    private final String invalidTokenResponse = "{"
            + "    \"message\": \"Token is invalid\","
            + "    \"token\": \"is invalid\","
            + "    \"success\": false,"
            + "    \"errors\": {"
            + "        \"message\": \"Token is invalid\""
            + "    },\n"
            + "    \"error_code\": \"60020\""
            + "}";

    private final String validTokenResponse = "{"
            + "    \"message\": \"Token is valid.\","
            + "    \"token\": \"is valid\","
            + "    \"success\": \"true\","
            + "    \"device\": {"
            + "        \"id\": null,"
            + "        \"os_type\": \"sms\","
            + "        \"registration_date\": 1500648405,"
            + "        \"registration_method\": null,"
            + "        \"registration_country\": null,"
            + "        \"registration_region\": null,"
            + "        \"registration_city\": null,"
            + "        \"country\": null,"
            + "        \"region\": null,"
            + "        \"city\": null,"
            + "        \"ip\": null,"
            + "        \"last_account_recovery_at\": 1494631010,"
            + "        \"last_sync_date\": null"
            + "    }"
            + "}";

    private final String invalidErrorFormatResponse = ""
            + "    \"message\": \"Token is invalid\","
            + "    \"token\": \"is invalid\","
            + "    \"success\": false,"
            + "    \"errors\": {"
            + "        \"message\": \"Token is invalid\""
            + "    },"
            + "    \"error_code\": \"60019\""
            + "}";

    @Before
    public void setUp() {
        tokens = new AuthyApiClient(testApiKey, testHost, true).getTokens();
    }

    @Test
    public void tokenFormatValidation() {
        String alphaToken = "abcde";
        try {
            tokens.verify(0, alphaToken);
            fail("Tokens must be numeric");
        } catch (Exception e) {
            Assert.assertTrue("Proper exception must be thrown", e instanceof AuthyException);
        }
    }

    @Test
    public void tokenLengthValidation() {
        String shortToken = "123";
        try {
            tokens.verify(0, shortToken);
            fail("Tokens must be between 6 and 10 digits");
        } catch (Exception e) {
            Assert.assertTrue("Proper exception must be thrown", e instanceof AuthyException);
        }

        String longToken = "12345678901";
        try {
            tokens.verify(0, longToken);
            fail("Tokens must be between 6 and 10 digits");
        } catch (Exception e) {
            Assert.assertTrue("Proper exception must be thrown", e instanceof AuthyException);
        }
    }

    @Test
    public void testInvalidTokenResponse() {
        stubFor(get(urlPathMatching("/protected/json/verify/.*"))
                .willReturn(aResponse()
                        .withStatus(HttpURLConnection.HTTP_UNAUTHORIZED)
                        .withHeader("Content-Type", "application/json")
                        .withBody(invalidTokenResponse)));


        try {
            tokens.verify(testUserId, testToken);
            fail("Exception should have been thrown");
        } catch (AuthyException e) {
            assertEquals(TOKEN_INVALID, e.getErrorCode());
            assertEquals(HttpURLConnection.HTTP_UNAUTHORIZED, e.getStatus());
        }
    }

    @Test
    public void testInvalidTokenLength() {
        try {
            tokens.verify(testUserId, "12345678901234567890");
            fail("Exception should have been thrown");
        } catch (AuthyException e) {
            assertEquals(TOKEN_INVALID, e.getErrorCode());
            assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, e.getStatus());
        }
    }

    @Test
    public void testInvalidTokenCharacters() {
        stubFor(get(urlPathMatching("/protected/json/verify/.*"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody(invalidTokenResponse)));


        try {
            tokens.verify(testUserId, "asdasdadasdasdasdasda");
            fail("Exception should have been thrown");
        } catch (AuthyException e) {
            assertEquals(TOKEN_INVALID, e.getErrorCode());
        }
    }

    @Test
    public void testValidTokenResponse() {
        stubFor(get(urlPathMatching("/protected/json/verify/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(validTokenResponse)));


        try {
            Token token = tokens.verify(testUserId, testToken);
            Assert.assertNull("Token must not have an error", token.getError());
            Assert.assertTrue("Token verification must be successful", token.isOk());
        } catch (AuthyException e) {
            fail("Verification should be successful");
        }
    }

    @Test
    public void testVerificationOptions() {
        stubFor(get(urlPathMatching("/protected/json/verify/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(validTokenResponse)));


        try {
            Map<String, String> options = new HashMap<>();
            options.put("force", "false");
            Token token = tokens.verify(testUserId, testToken, options);
            Assert.assertNull("Token must not have an error", token.getError());
            Assert.assertTrue("Token verification must be successful", token.isOk());
        } catch (AuthyException e) {
            fail("Verification should be successful");
        }
    }

    @Test
    public void testInvalidErrorFormatResponse() {
        stubFor(get(urlPathMatching("/protected/json/verify/.*"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody(invalidErrorFormatResponse)));


        try {
            tokens.verify(testUserId, testToken);
            fail("Exception must be thrown");
        } catch (AuthyException e) {
            return;
        }
        fail("Proper exception must be thrown");
    }

    @Test
    public void testRequestParameters() {
        stubFor(get(urlPathMatching("/protected/json/verify/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(validTokenResponse)));


        try {
            Token token = tokens.verify(testUserId, testToken);

            verify(getRequestedFor(urlPathEqualTo("/protected/json/verify/" + testToken + "/" + testUserId))
                    .withHeader("X-Authy-API-Key", equalTo(testApiKey)));
            Assert.assertNull("Token must not have an error", token.getError());
            Assert.assertTrue("Token verification must be successful", token.isOk());
        } catch (AuthyException e) {
            fail("Verification should be successful");
        }
    }
}