package com.authy.api; /**
 * Created by hansospina on 12/12/16.
 */

import com.authy.AuthyApiClient;
import com.authy.AuthyUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class TestSMSCode {


    private static Properties properties;
    private AuthyApiClient client;

    @Before
    public void setUp() throws IOException {

        properties = AuthyUtil.loadProperties("test.properties", TestSMSCode.class);

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
        // smart enough to decide if it sends the sms or just notifies the user inside the app
        Hash tmp = client.getUsers().requestSms(Integer.parseInt(properties.getProperty("user_id")), map);
        // isOK() is the method that will allow you to know if the request worked.
        Assert.assertTrue(tmp.isOk());
        // there's also a response message.
        Assert.assertEquals(properties.getProperty("sms_ask_response_message"), tmp.getMessage());
    }

    @Test
    public void testBadRequestSMS() {
        // let's setup some extra parameters
        HashMap<String, String> map = new HashMap<>();
        // We are testing SMS here so let's add the force param to have Authy send the SMS even if the
        // user has the Authy App installed
        map.put("force", "true");
        // This is the API normal call you will do to send an SMS, if we don't pass the force option authy will be
        // smart enough to decide if it sends the sms or just notifies the user inside the app
        Hash tmp = client.getUsers().requestSms(0, map);
        // isOK() is the method that will allow you to know if the request worked.
        Assert.assertFalse(tmp.isOk());
        // there's also a response message.
        Assert.assertEquals(properties.getProperty("sms_ask_response_message"), tmp.getMessage());
    }

    @Test
    public void testRequestSMSNoForce() {
        // let's setup some extra parameters
        HashMap<String, String> map = new HashMap<>();
        // This is the API normal call you will do to send an SMS, if we don't pass the force option authy will be
        // smart enough to decide if it sends the sms or just notifies the user inside the app
        Hash tmp = client.getUsers().requestSms(Integer.parseInt(properties.getProperty("user_id")));
        // isOK() is the method that will allow you to know if the request worked.
        Assert.assertTrue(tmp.isOk());
        // there's also a response message.
        Assert.assertEquals("Ignored: SMS is not needed for smartphones. Pass force=true if you want to actually send it anyway.", tmp.getMessage());
    }


}
