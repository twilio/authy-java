package com.authy.api;

import com.authy.AuthyUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

public class TestPhoneVerification {

    private PhoneVerification client;

    @Before
    public void setUp() throws IOException {

        Properties properties = AuthyUtil.loadProperties("test.properties", TestPhoneVerification.class);

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
        Verification result = client.start("+1 775-461-5609", "1", "sms", params);

        Assert.assertEquals("Text message sent to +1 775-461-5609.", result.getMessage());
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
        Verification result = client.check("+1 775-461-5609", "1", "2061");
        Assert.assertEquals("Verification code is incorrect", result.getMessage());
        Assert.assertEquals("false", result.getSuccess());
    }

}
