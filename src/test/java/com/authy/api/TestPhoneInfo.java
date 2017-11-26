package com.authy.api;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class TestPhoneInfo {
    private static final String successReponse = "{\n" +
            "    \"message\": \"Phone number information as of 2017-11-25 23:21:39 UTC\",\n" +
            "    \"type\": \"voip\",\n" +
            "    \"provider\": \"Pinger\",\n" +
            "    \"ported\": false,\n" +
            "    \"success\": true\n" +
            "}";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(18089);

    @Test
    public void testsPhoneInfo() {
        stubFor(get(urlPathEqualTo("/protected/json/phones/info"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=utf-8")
                        .withBody(successReponse)));
        final String testApiKey = "test_api_key";
        final PhoneInfo client = new PhoneInfo("http://localhost:18089", testApiKey, true);

        final String phoneNumber = "7754615609";
        final String countryCode = "1";
        final PhoneInfoResponse result = client.info(phoneNumber, countryCode);

        verify(getRequestedFor(urlPathEqualTo("/protected/json/phones/info"))
                .withHeader("X-Authy-API-Key", equalTo(testApiKey))
                .withQueryParam("phone_number", equalTo(phoneNumber))
                .withQueryParam("country_code", equalTo(countryCode)));
        Assert.assertEquals(true, result.getMessage().contains("Phone number information as of"));
        Assert.assertEquals("Pinger", result.getProvider());
        Assert.assertEquals("voip", result.getType());
        Assert.assertEquals("false", result.getIsPorted());
        Assert.assertEquals("true", result.getSuccess());
    }
}
