package com.authy.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class TestPhoneVerification extends TestApiBase {

    private PhoneVerification client;

    final private String getStartSuccessResponse(final String message){
        return "{\n" +
                "    \"carrier\": \"Pinger - Bandwidth.com - Sybase365\",\n" +
                "    \"is_cellphone\": false,\n" +
                "    \"message\": \""+ message + "\",\n" +
                "    \"seconds_to_expire\": 599,\n" +
                "    \"uuid\": \"bec828c0-b535-0135-8e26-1226b57fac04\",\n" +
                "    \"success\": true\n" +
                "}";
    }

    final private String startInvalidNumberResponse = "{\n" +
            "    \"error_code\": \"60033\",\n" +
            "    \"message\": \"Phone number is invalid\",\n" +
            "    \"errors\": {\n" +
            "        \"message\": \"Phone number is invalid\"\n" +
            "    },\n" +
            "    \"success\": false\n" +
            "}";

    final private String checkIncorrectVerificationResponse = "{\n" +
            "    \"error_code\": \"60022\",\n" +
            "    \"message\": \"Verification code is incorrect\",\n" +
            "    \"errors\": {\n" +
            "        \"message\": \"Verification code is incorrect\"\n" +
            "    },\n" +
            "    \"success\": false\n" +
            "}";

    final private String checkCorrectVerificationResponse = "{\n" +
            "    \"message\": \"Verification code is correct.\",\n" +
            "    \"success\": true\n" +
            "}";

    @Before
    public void setUp() {
        client = new PhoneVerification(testHost, testApiKey, true);
    }

    @Test
    public void testContentTypeToBeJson() {
        Assert.assertEquals("application/json", client.getContentType());
    }

    @Test
    public void testVerificationStartEs() {
        stubFor(post(urlPathEqualTo("/protected/json/phones/verification/start"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=utf-8")
                        .withBody(getStartSuccessResponse("Llamada a +1 775-461-5609 fue iniciada."))));

        Params params = new Params();
        params.setAttribute("locale", "es");
        Verification result = client.start("775-461-5609", "1", "call", params);

        verify(postRequestedFor(urlPathEqualTo("/protected/json/phones/verification/start"))
                .withHeader("X-Authy-API-Key", equalTo(testApiKey))
                .withRequestBody(equalToJson("{\"country_code\": \"1\", \"phone_number\": \"775-461-5609\", \"locale\": \"es\", \"via\": \"call\"}", true, true)));
        Assert.assertEquals("Llamada a +1 775-461-5609 fue iniciada.", result.getMessage());
        Assert.assertEquals("true", result.getSuccess());
    }

    @Test
    public void testVerificationStartEn() {
        stubFor(post(urlPathEqualTo("/protected/json/phones/verification/start"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=utf-8")
                        .withBody(getStartSuccessResponse("Text message sent to +1 775-461-5609."))));

        Params params = new Params();
        params.setAttribute("locale", "en");
        Verification result = client.start("775-461-5609", "1", "sms", params);

        verify(postRequestedFor(urlPathEqualTo("/protected/json/phones/verification/start"))
                .withHeader("X-Authy-API-Key", equalTo(testApiKey))
                .withRequestBody(equalToJson("{\"country_code\": \"1\", \"phone_number\": \"775-461-5609\", \"locale\": \"en\", \"via\": \"sms\"}", true, true)));
        String msg = "Text message sent to +1 775-461-5609.";
        Assert.assertEquals(msg, result.getMessage());
        Assert.assertEquals("true", result.getSuccess());
    }

    @Test
    public void testVerificationStartEnInvalid() {
        stubFor(post(urlPathEqualTo("/protected/json/phones/verification/start"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody(startInvalidNumberResponse)));

        Params params = new Params();
        params.setAttribute("locale", "en");

        Verification result = client.start("282-23", "1", "sms", params);
        Assert.assertEquals("Phone number is invalid", result.getMessage());
        Assert.assertEquals("false", result.getSuccess());
    }

    @Test
    public void testVerificationCheckSuccess(){
        stubFor(get(urlPathEqualTo("/protected/json/phones/verification/check"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(checkCorrectVerificationResponse)));

        Verification result = client.check("775-461-5609", "1", "2061");

        Assert.assertEquals("Verification code is correct.", result.getMessage());
        Assert.assertEquals("true", result.getSuccess());
    }

    @Test
    public void testVerificationCheckIncorrectCode() {
        stubFor(get(urlPathEqualTo("/protected/json/phones/verification/check"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody(checkIncorrectVerificationResponse)));

        Verification result = client.check("775-461-5609", "1", "2061");

        Assert.assertEquals("Verification code is incorrect", result.getMessage());
        Assert.assertEquals("false", result.getSuccess());
    }
}
