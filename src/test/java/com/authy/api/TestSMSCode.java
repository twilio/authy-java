package com.authy.api; /**
 * Created by hansospina on 12/12/16.
 */

import com.authy.AuthyApiClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class TestSMSCode {

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
    public void testRequestSMS() {
        Assert.assertNotNull(properties.getProperty("user_id"));
        // let's setup some extra parameters
        HashMap<String, String> map = new HashMap<String, String>();
        // We are testing SMS here so let's add the force param to have Authy send the SMS even if the
        // user has the Authy App installed
        map.put("force", "true");
        // This is the API normal call you will do to send an SMS, if we don't pass the force option authy will be
        // smart enough to decide if it sends the sms or just notifies the user insdie the app
        Hash tmp = client.getUsers().requestSms(Integer.parseInt(properties.getProperty("user_id")), map);
        // isOK() is the method that will allow you to know if the request worked.
        Assert.assertTrue(tmp.isOk());
        // there's also a response message.
        Assert.assertEquals(properties.getProperty("sms_ask_response_message"), tmp.getMessage());
    }

    @Test
    public void testBadRequestSMS() {
        // let's setup some extra parameters
        HashMap<String, String> map = new HashMap<String, String>();
        // We are testing SMS here so let's add the force param to have Authy send the SMS even if the
        // user has the Authy App installed
        map.put("force", "true");
        // This is the API normal call you will do to send an SMS, if we don't pass the force option authy will be
        // smart enough to decide if it sends the sms or just notifies the user insdie the app
        Hash tmp = client.getUsers().requestSms(0, map);
        // isOK() is the method that will allow you to know if the request worked.
        Assert.assertFalse(tmp.isOk());
        // there's also a response message.
        Assert.assertNotEquals(properties.getProperty("sms_ask_response_message"), tmp.getMessage());
    }


}
