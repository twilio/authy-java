package com.authy.api;

import com.authy.AuthyUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

public class TestPhoneInfo {

    private static Properties properties;
    private PhoneInfo client;

    @Before
    public void setUp() throws IOException {

        Properties properties = AuthyUtil.loadProperties("test.properties", TestPhoneInfo.class);

        // Let's configure the API Client with the properties defined at the test.properties file.
        org.junit.Assert.assertNotNull(properties.getProperty("api_key"));
        org.junit.Assert.assertNotNull(properties.getProperty("api_url"));

        client = new PhoneInfo(properties.getProperty("api_url"), properties.getProperty("api_key"), true);
    }

    @Test
    public void itTestsPhoneInfo() {

        PhoneInfoResponse result = client.info("7754615609", "1");

        Assert.assertEquals(true, result.getMessage().contains("Phone number information as of"));
        Assert.assertEquals("Pinger/TextFree", result.getProvider());
        Assert.assertEquals("voip", result.getType());
        Assert.assertEquals("false", result.getIsPorted());
        Assert.assertEquals("true", result.getSuccess());
    }

}
