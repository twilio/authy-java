package com.authy.api;

import com.authy.AuthyApiClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class TestSMSCode extends TestApiBase {

    private AuthyApiClient client;
    final private String testUserId = "30144611";

    private final String successResponseForced = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<hash>\n" +
            "    <success type=\"boolean\">true</success>\n" +
            "    <message>SMS token was sent</message>\n" +
            "    <cellphone>+57-XXX-XXX-XX12</cellphone>\n" +
            "</hash>";

    private final String userNotFoundResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<errors>\n" +
            "    <message>User not found.</message>\n" +
            "    <error-code>60026</error-code>\n" +
            "</errors>";

    private final String successResponseNotForced = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<hash>\n" +
            "    <message>Ignored: SMS is not needed for smartphones. Pass force=true if you want to actually send it anyway.</message>\n" +
            "    <cellphone>+57-XXX-XXX-XX12</cellphone>\n" +
            "    <device>android</device>\n" +
            "    <ignored type=\"boolean\">true</ignored>\n" +
            "    <success type=\"boolean\">true</success>\n" +
            "</hash>";

    @Before
    public void setUp() {
        client = new AuthyApiClient(testApiKey, testHost, true);
    }

    @Test
    public void testRequestSMS() {
        stubFor(get(urlPathEqualTo("/protected/xml/sms/" + testUserId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/xml")
                        .withBody(successResponseForced)));

        // let's setup some extra parameters
        HashMap<String, String> map = new HashMap<>();
        // We are testing SMS here so let's add the force param to have Authy send the SMS even if the
        // user has the Authy App installed
        map.put("force", "true");
        // This is the API normal call you will do to send an SMS, if we don't pass the force option authy will be
        // smart enough to decide if it sends the sms or just notifies the user inside the app
        Hash response = client.getUsers().requestSms(Integer.parseInt(testUserId), map);

        verify(getRequestedFor(urlPathEqualTo("/protected/xml/sms/" + testUserId))
                .withHeader("X-Authy-API-Key", equalTo(testApiKey))
                .withQueryParam("force", equalTo("true")));
        // isOK() is the method that will allow you to know if the request worked.
        Assert.assertTrue(response.isOk());
        // there's also a response message.
        Assert.assertEquals("SMS token was sent", response.getMessage());
    }

    @Test
    public void testUserNotFoundSMS() {
        final Integer badUserId = 0;
        stubFor(get(urlPathEqualTo("/protected/xml/sms/" + badUserId))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/xml")
                        .withBody(userNotFoundResponse)));

        // let's setup some extra parameters
        HashMap<String, String> map = new HashMap<>();
        // We are testing SMS here so let's add the force param to have Authy send the SMS even if the
        // user has the Authy App installed
        map.put("force", "true");
        // This is the API normal call you will do to send an SMS, if we don't pass the force option authy will be
        // smart enough to decide if it sends the sms or just notifies the user inside the app
        Hash reponse = client.getUsers().requestSms(badUserId, map);

        verify(getRequestedFor(urlPathEqualTo("/protected/xml/sms/" + badUserId))
                .withHeader("X-Authy-API-Key", equalTo(testApiKey))
                .withQueryParam("force", equalTo("true")));
        // isOK() is the method that will allow you to know if the request worked.
        Assert.assertFalse(reponse.isOk());
    }

    @Test
    public void testRequestSMSNoForce() {
        stubFor(get(urlPathEqualTo("/protected/xml/sms/" + testUserId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/xml")
                        .withBody(successResponseNotForced)));

        // This is the API normal call you will do to send an SMS, if we don't pass the force option authy will be
        // smart enough to decide if it sends the sms or just notifies the user inside the app
        Hash response = client.getUsers().requestSms(Integer.parseInt(testUserId));

        verify(getRequestedFor(urlPathEqualTo("/protected/xml/sms/" + testUserId))
                .withHeader("X-Authy-API-Key", equalTo(testApiKey)));
        // isOK() is the method that will allow you to know if the request worked.
        Assert.assertTrue(response.isOk());
        // there's also a response message.
        Assert.assertEquals("Ignored: SMS is not needed for smartphones. Pass force=true if you want to actually send it anyway.", response.getMessage());
    }


}
