package com.authy.api;

import static com.authy.api.Error.Code.PHONE_INFO_ERROR_QUERYING;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

import static junit.framework.TestCase.assertEquals;

import com.authy.AuthyException;

import org.junit.Test;

public class TestPhoneInfo extends TestApiBase {
    private static final String successResponse = "{" +
            "    \"message\": \"Phone number information as of 2017-11-25 23:21:39 UTC\"," +
            "    \"type\": \"voip\"," +
            "    \"provider\": \"Pinger\"," +
            "    \"ported\": false," +
            "    \"success\": true" +
            "}";

    final private String phoneInfoError = "{" +
            "    \"error_code\": \"60025\"," +
            "    \"message\": \"Server error while querying phone information. Please try again later\"," +
            "    \"errors\": {" +
            "        \"message\": \"Server error while querying phone information. Please try again later\"" +
            "    }," +
            "    \"success\": false" +
            "}";

    @Test
    public void testPhoneInfo() throws AuthyException {
        stubFor(get(urlPathEqualTo("/protected/json/phones/info"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=utf-8")
                        .withBody(successResponse)));
        final PhoneInfo client = new PhoneInfo(testHost, testApiKey, true);

        final String phoneNumber = "7754615609";
        final String countryCode = "1";
        final PhoneInfoResponse result = client.info(phoneNumber, countryCode);

        verify(getRequestedFor(urlPathEqualTo("/protected/json/phones/info"))
                .withHeader("X-Authy-API-Key", equalTo(testApiKey))
                .withQueryParam("phone_number", equalTo(phoneNumber))
                .withQueryParam("country_code", equalTo(countryCode)));
        assertEquals(true, result.getMessage().contains("Phone number information as of"));
        assertEquals("Pinger", result.getProvider());
        assertEquals("voip", result.getType());
        assertEquals("false", result.getIsPorted());
        assertEquals("true", result.getSuccess());
    }

    @Test
    public void testPhoneInfoError() throws AuthyException {
        stubFor(get(urlPathEqualTo("/protected/json/phones/info"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody(phoneInfoError)));
        final PhoneInfo client = new PhoneInfo(testHost, testApiKey, true);

        final String phoneNumber = "7754615609";
        final String countryCode = "1";
        final PhoneInfoResponse result = client.info(phoneNumber, countryCode);

        assertEquals(true, result.getMessage().contains("Server error while querying phone information"));
        assertEquals("false", result.getSuccess());
        assertEquals(PHONE_INFO_ERROR_QUERYING, result.getError().getCode());
    }
}
