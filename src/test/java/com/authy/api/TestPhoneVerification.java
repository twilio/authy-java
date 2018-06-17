package com.authy.api;

import com.authy.AuthyException;
import org.junit.Before;
import org.junit.Test;

import static com.authy.api.Error.Code.INVALID_PHONE_NUMBER;
import static com.authy.api.Error.Code.PHONE_VERIFICATION_INCORRECT;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

public class TestPhoneVerification extends TestApiBase {

    private PhoneVerification client;

    private String getStartSuccessResponse(final String message){
        return "{" +
                "    \"carrier\": \"Pinger - Bandwidth.com - Sybase365\"," +
                "    \"is_cellphone\": false," +
                "    \"message\": \""+ message + "\"," +
                "    \"seconds_to_expire\": 599," +
                "    \"uuid\": \"bec828c0-b535-0135-8e26-1226b57fac04\"," +
                "    \"success\": true" +
                "}";
    }

     private String startInvalidNumberResponse = "{" +
            "    \"error_code\": \"60033\"," +
            "    \"message\": \"Phone number is invalid\"," +
            "    \"errors\": {" +
            "        \"message\": \"Phone number is invalid\"" +
            "    }," +
            "    \"success\": false" +
            "}";

    private String checkIncorrectVerificationResponse = "{" +
            "    \"error_code\": \"60022\"," +
            "    \"message\": \"Verification code is incorrect\"," +
            "    \"errors\": {" +
            "        \"message\": \"Verification code is incorrect\"" +
            "    }," +
            "    \"success\": false" +
            "}";

    private String checkCorrectVerificationResponse = "{" +
            "    \"message\": \"Verification code is correct.\"," +
            "    \"success\": true" +
            "}";

    @Before
    public void setUp() {
        client = new PhoneVerification(testHost, testApiKey, true);
    }

    @Test
    public void testContentTypeToBeJson() {
        assertEquals("application/json", client.getContentType());
    }

    @Test
    public void testVerificationStartEs() throws AuthyException {
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
        assertEquals("Llamada a +1 775-461-5609 fue iniciada.", result.getMessage());
        assertEquals("true", result.getSuccess());
    }

    @Test
    public void testVerificationStartEn() throws AuthyException {
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
        assertEquals(msg, result.getMessage());
        assertEquals("true", result.getSuccess());
    }

    @Test
    public void testVerificationStartEnInvalid() throws AuthyException {
        stubFor(post(urlPathEqualTo("/protected/json/phones/verification/start"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody(startInvalidNumberResponse)));

        Params params = new Params();
        params.setAttribute("locale", "en");

        Verification result = client.start("282-23", "1", "sms", params);

        assertEquals("Phone number is invalid", result.getMessage());
        assertEquals("false", result.getSuccess());
        Error error = result.getError();
        assertNotNull(error);
        assertEquals(INVALID_PHONE_NUMBER, error.getCode());
    }

    @Test
    public void testVerificationCheckSuccess() throws AuthyException {
        stubFor(get(urlPathEqualTo("/protected/json/phones/verification/check"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(checkCorrectVerificationResponse)));

        Verification result = client.check("775-461-5609", "1", "2061");

        assertEquals("Verification code is correct.", result.getMessage());
        assertTrue(result.isOk());
        assertEquals("true", result.getSuccess());
    }

    @Test
    public void testVerificationCheckIncorrectCode() throws AuthyException {
        stubFor(get(urlPathEqualTo("/protected/json/phones/verification/check"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody(checkIncorrectVerificationResponse)));

        Verification result = client.check("775-461-5609", "1", "2061");

        assertEquals("Verification code is incorrect", result.getMessage());
        assertFalse(result.isOk());
        assertEquals("false", result.getSuccess());
        Error error = result.getError();
        assertNotNull(error);
        assertEquals(PHONE_VERIFICATION_INCORRECT, error.getCode());
    }
}
