package com.authy.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestPhoneVerification {

    private static Properties properties;

    static {

        InputStream in = TestSMSCode.class.getResourceAsStream("test.properties");
        properties = new Properties();

        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PhoneVerification client;

    @Before
    public void setUp() throws IOException {
        // Let's configure the API Client with the properties defined at the test.properties file.
        Assert.assertNotNull(properties.getProperty("api_key"));
        Assert.assertNotNull(properties.getProperty("api_url"));
        client = new PhoneVerification(properties.getProperty("api_url"), properties.getProperty("api_key"), true);
    }

    @Test
    public void itChecksContentTypeToBeJson() {
        Assert.assertEquals("application/json", client.getContentType());
    }

    @Test
    public void itTestsVerificationStartEs() {
        Params params = new Params();
        params.setAttribute("locale", "es");

        Verification result = client.start("+1 775-461-5609", "1", "call", params);
        Assert.assertEquals("Llamada a +1 775-461-5609 fue iniciada.", result.getMessage());
        Assert.assertEquals("true", result.getSuccess());
    }

    @Test
    public void itTestsVerificationStartEn() {
        Params params = new Params();
        params.setAttribute("locale", "en");
        Verification result = client.start(properties.getProperty("phone_verification_test_number"), "1", "sms", params);

        String msg = "Text message sent to " + properties.getProperty("phone_verification_test_number") + ".";
        Assert.assertEquals(msg, result.getMessage());
        Assert.assertEquals("true", result.getSuccess());
    }

    @Test
    public void itTestsVerificationStartEnInvalid() {
        Params params = new Params();
        params.setAttribute("locale", "en");

        Verification result = client.start("282-23", "1", "sms", params);
        Assert.assertEquals("Phone number is invalid", result.getMessage());
        Assert.assertEquals("false", result.getSuccess());
    }

    @Test
    public void itTestsVerificationCheckIncorrectCode() {
        Verification result = client.check(properties.getProperty("phone_verification_test_number"), "1", "2061");
        Assert.assertEquals("Verification code is incorrect", result.getMessage());
        Assert.assertEquals("false", result.getSuccess());
    }

}