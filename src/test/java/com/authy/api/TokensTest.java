package com.authy.api;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

import static junit.framework.TestCase.fail;

import com.authy.AuthyApiClient;
import com.authy.AuthyException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class TokensTest extends TestApiBase {

    private Tokens tokens;
    private int testUserId = 123456;
    private String testToken = "123456";

    private final String invalidTokenResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<errors>"
            + "    <message>Token is invalid.</message>"
            + "    <success>false</success>"
            + "    <error-code>60019</error-code>"
            + "</errors>";

    private final String validTokenResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<hash>"
            + "    <message>Token is valid.</message>"
            + "    <token>is valid</token>"
            + "    <success>true</success>"
            + "    <device>"
            + "        <city nil=\"true\"/>"
            + "        <country>US</country>"
            + "        <ip>186.112.233.58</ip>"
            + "        <region nil=\"true\"/>"
            + "        <registration-city nil=\"true\"/>"
            + "        <registration-country>US</registration-country>"
            + "        <registration-ip>186.112.233.58</registration-ip>"
            + "        <registration-method>sms</registration-method>"
            + "        <registration-region nil=\"true\"/>"
            + "        <os-type>android</os-type>"
            + "        <last-account-recovery-at nil=\"true\"/>"
            + "        <id type=\"integer\">2179876</id>"
            + "        <registration-date type=\"integer\">1513879290</registration-date>"
            + "        <last-sync-date type=\"integer\">1514304913</last-sync-date>"
            + "    </device>"
            + "</hash>";

    private final String invalidErrorFormatResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<error>"
            + "    <message>Token is invalid.</message>"
            + "    <success>false</success>"
            + "    <error-code>60019</error-code>"
            + "</error>";

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
        stubFor(get(urlPathMatching("/protected/xml/verify/.*"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/xml")
                        .withBody(invalidTokenResponse)));


        try {
            Token token = tokens.verify(testUserId, testToken);
            Assert.assertNotNull("Token must have an error", token.getError());
        } catch (UnsupportedEncodingException | AuthyException e) {
            fail("Token should have an error object");
        }
    }

    @Test
    public void testValidTokenResponse() {
        stubFor(get(urlPathMatching("/protected/xml/verify/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/xml")
                        .withBody(validTokenResponse)));


        try {
            Token token = tokens.verify(testUserId, testToken);
            Assert.assertNull("Token must not have an error", token.getError());
            Assert.assertTrue("Token verification must be successful", token.isOk());
        } catch (UnsupportedEncodingException | AuthyException e) {
            fail("Verification should be successful");
        }
    }

    @Test
    public void testVerificationOptions() {
        stubFor(get(urlPathMatching("/protected/xml/verify/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/xml")
                        .withBody(validTokenResponse)));


        try {
            Map<String, String> options = new HashMap<>();
            options.put("force", "false");
            Token token = tokens.verify(testUserId, testToken, options);
            Assert.assertNull("Token must not have an error", token.getError());
            Assert.assertTrue("Token verification must be successful", token.isOk());
        } catch (UnsupportedEncodingException | AuthyException e) {
            fail("Verification should be successful");
        }
    }

    @Test
    public void testInvalidErrorFormatResponse() {
        stubFor(get(urlPathMatching("/protected/xml/verify/.*"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/xml")
                        .withBody(invalidErrorFormatResponse)));


        try {
            tokens.verify(testUserId, testToken);
            fail("Exception must be thrown");
        } catch (UnsupportedEncodingException | AuthyException e) {
            Assert.assertTrue("Proper exception must be thrown", e instanceof AuthyException);
        }
    }

}