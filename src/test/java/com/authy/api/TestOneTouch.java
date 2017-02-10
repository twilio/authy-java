package com.authy.api;

import com.authy.AuthyApiClient;
import com.authy.AuthyException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.json.JSONObject;

/**
 * Unit tests for the API described at: http://docs.authy.com/onetouch.html#onetouch-api
 *
 * @author hansospina
 *
 * Copyright © 2016 Twilio, Inc. All Rights Reserved.
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
        OneTouchOptionParams options = new OneTouchOptionParams();
        options.addDetail("username", "User");
        options.addDetail("location", "California,USA");
        options.addHiddenDetails("ip_address", "10.10.3.203");
        options.addLogo(Logo.Resolution.Low, "http://image.co");
        
        OneTouchOptionParams optionsWithEmptytDetail = new OneTouchOptionParams();
        optionsWithEmptytDetail.addHiddenDetails("ip_address", "10.10.3.203");
        optionsWithEmptytDetail.addLogo(Logo.Resolution.Low, "http://image.co");
        
        OneTouchOptionParams optionsWithEmptytHidden = new OneTouchOptionParams();
        optionsWithEmptytHidden.addDetail("username", "User");
        optionsWithEmptytHidden.addDetail("location", "California,USA");
        optionsWithEmptytHidden.addLogo(Logo.Resolution.Low, "http://image.co");
        
        
        
        HashMap<String, String> details = new HashMap<String, String>();
        details.put("username", "User");
        details.put("location", "California,USA");

        HashMap<String, String> hidden = new HashMap<String, String>();
        hidden.put("ip_address", "10.10.3.203");

        List<Logo> logos = new ArrayList<>();
        logos.add(new Logo(Logo.Resolution.High, "http://image.co"));
        logos.add(new Logo(Logo.Resolution.Medium, "http://image.co"));

        
        HashMap<String, Object> optionsWithoutDetail=new HashMap<>();
        optionsWithoutDetail.put("hidden_details", hidden);
        optionsWithoutDetail.put("logos", logos);
        
        HashMap<String, Object> optionsDetailsNotIsMap=new HashMap<>();
        optionsDetailsNotIsMap.put("details", new JSONObject());
        optionsDetailsNotIsMap.put("hidden_details", hidden);
        optionsDetailsNotIsMap.put("logos", logos);
        
        HashMap<String, Object> optionsWithoutHidden=new HashMap<>();
        optionsWithoutHidden.put("details", details);
        optionsWithoutHidden.put("logos", logos);
        
        HashMap<String, Object> optionsWithNoLogoList=new HashMap<>();
        optionsWithNoLogoList.put("details", details);
        optionsWithNoLogoList.put("hidden_details", hidden);
        optionsWithNoLogoList.put("logos", "StringLogo");
        
        HashMap<String, Object> optionsWithWrongListElement=new HashMap<>();
        optionsWithWrongListElement.put("details", details);
        optionsWithWrongListElement.put("hidden_details", hidden);
        List<HashMap<String,String>> badLogos=new ArrayList<>();
        HashMap<String, String> logo=new HashMap<>();
        logo.put("resol", "res");
        badLogos.add(logo);
        optionsWithWrongListElement.put("logos", badLogos);
        
        
        //test no details sent
        OneTouchResponse response = client.getOneTouch().sendApprovalRequest(Integer.parseInt(properties.getProperty("user_id")), "Authorize OneTouch Unit Test", optionsWithoutDetail, 120);
        Assert.assertFalse(response.isSuccess());


        //Test empty details
        response = client.getOneTouch().sendApprovalRequest(Integer.parseInt(properties.getProperty("user_id")), "Authorize OneTouch Unit Test", optionsWithEmptytDetail.generateParams(), 120);
        Assert.assertFalse(response.isSuccess());
        
        //Test  details has not hashmap
        response = client.getOneTouch().sendApprovalRequest(Integer.parseInt(properties.getProperty("user_id")), "Authorize OneTouch Unit Test", optionsDetailsNotIsMap, 120);
        Assert.assertFalse(response.isSuccess());
       
        //test no hidden sent
        response = client.getOneTouch().sendApprovalRequest(Integer.parseInt(properties.getProperty("user_id")), "Authorize OneTouch Unit Test", optionsWithoutHidden, 120);
        Assert.assertFalse(response.isSuccess());


        //Test empty hidden
        response = client.getOneTouch().sendApprovalRequest(Integer.parseInt(properties.getProperty("user_id")), "Authorize OneTouch Unit Test", optionsWithEmptytHidden.generateParams(), 120);
        Assert.assertFalse(response.isSuccess());
        
        //Test logos is not a List
        response = client.getOneTouch().sendApprovalRequest(Integer.parseInt(properties.getProperty("user_id")), "Authorize OneTouch Unit Test", optionsWithNoLogoList, 120);
        Assert.assertFalse(response.isSuccess());
        
        //Test the logos list has not element of Logo.class
        response = client.getOneTouch().sendApprovalRequest(Integer.parseInt(properties.getProperty("user_id")), "Authorize OneTouch Unit Test", optionsWithWrongListElement, 120);
        Assert.assertFalse(response.isSuccess());

        // test no send userId 
        response = client.getOneTouch().sendApprovalRequest(null, "Authorize OneTouch Unit Test", options.generateParams(), 120);
        Assert.assertFalse(response.isSuccess());
        
        
        // test all ok
        response = client.getOneTouch().sendApprovalRequest(Integer.parseInt(properties.getProperty("user_id")), "Authorize OneTouch Unit Test", options.generateParams(), 120);
        System.out.println(response.getMessage());

        Assert.assertTrue(response.isSuccess());
        Assert.assertNotNull(response.getApprovalRequest().getUUID());
        // there should be no error code
        Assert.assertEquals("Expected Error Code to be empty but got: [" + response.getErrorCode() + "]", "", response.getErrorCode());

        
     }

    @Test
    public void testGetApprovalRequestStatus() {
        //Check that the properties file has a valid
        Assert.assertNotNull(properties.getProperty("onetouch_uuid"));

        OneTouchResponse response = client.getOneTouch().getApprovalRequestStatus(properties.getProperty("onetouch_uuid"));
        Assert.assertTrue(response.isSuccess());
        Assert.assertNotNull(response.getApprovalRequest().getStatus());

    }

}
