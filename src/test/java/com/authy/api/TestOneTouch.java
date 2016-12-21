package com.authy.api;

import com.authy.AuthyApiClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * Unit tests for the API described at: http://docs.authy.com/onetouch.html#onetouch-api
 *
 * @author hansospina
 *
 */
public class TestOneTouch {

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

    private AuthyApiClient client;

    @Before
    public void setUp() throws IOException {
        // Let's configure the API Client with the properties defined at the test.properties file.
        Assert.assertNotNull(properties.getProperty("api_key"));
        Assert.assertNotNull(properties.getProperty("api_url"));
        client = new AuthyApiClient(properties.getProperty("api_key"), properties.getProperty("api_url"), true);
    }

    @Test
    public void testSendApprovalRequest() {
        //Check that the properties file has a valid
        Assert.assertNotNull(properties.getProperty("user_id"));
        // let's setup some extra parameters
        HashMap<String, String> details = new HashMap<String, String>();
        details.put("username", "User");
        details.put("location", "California,USA");

        HashMap<String, String> hidden = new HashMap<String, String>();
        hidden.put("ip_address", "10.10.3.203");

        HashMap<String, String> logos = new HashMap<String, String>();

        OneTouchResponse response = client.getOneTouch().sendApprovalRequest(Integer.parseInt(properties.getProperty("user_id")), "Authorize OneTouch Unit Test", details, hidden, logos, 120);

        Assert.assertTrue(response.isSuccess());
        Assert.assertNotNull(response.getApprovalRequest());
        Assert.assertNotNull(response.getApprovalRequest().getUUID());
        // there should be no error code
        Assert.assertEquals("Expected Error Code to be empty but got: [" + response.getErrorCode() + "]", "", response.getErrorCode());

     }

    @Test
    public void testGetApprovalRequestStatus() {
        //Check that the properties file has a valid
        Assert.assertNotNull(properties.getProperty("onetouch_udid"));

        OneTouchResponse response = client.getOneTouch().getApprovalRequestStatus(properties.getProperty("onetouch_udid"));
        Assert.assertTrue(response.isSuccess());
    }

}
