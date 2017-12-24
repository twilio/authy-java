package com.authy.api;

import com.authy.AuthyApiClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class TestUsers extends TestApiBase {

    private Users client;
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

    private final String successCreateUserResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<hash>\n" +
            "    <message>User created successfully.</message>\n" +
            "    <user>\n" +
            "        <id type=\"integer\">1000</id>\n" +
            "    </user>\n" +
            "    <success type=\"boolean\">true</success>\n" +
            "</hash>"; ;

    @Before
    public void setUp() {
        client = new AuthyApiClient(testApiKey, testHost, true).getUsers();
    }

    @Test
    public void testCreateUser(){
        stubFor(post(urlPathEqualTo("/protected/xml/users/new"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/xml")
                        .withBody(successCreateUserResponse)));

        final User user = client.createUser("test@example.com", "3003003333", "57");

        verify(postRequestedFor(urlPathEqualTo("/protected/xml/users/new" ))
                .withHeader("X-Authy-API-Key", equalTo(testApiKey))
                .withHeader("Content-Type", equalTo("application/xml"))
                .withRequestBody(equalToXml("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                        "<user>" +
                        "<cellphone>3003003333</cellphone>" +
                        "<country_code>57</country_code>" +
                        "<email>test@example.com</email>" +
                        "</user>")));
        Assert.assertEquals(1000, user.getId());
        Assert.assertTrue(user.isOk());
    }

    @Test
    public void testCreateUserDefaultCountry(){
        stubFor(post(urlPathEqualTo("/protected/xml/users/new"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/xml")
                        .withBody(successCreateUserResponse)));

        final User user = client.createUser("test@example.com", "3003003333");

        verify(postRequestedFor(urlPathEqualTo("/protected/xml/users/new" ))
                .withHeader("X-Authy-API-Key", equalTo(testApiKey))
                .withHeader("Content-Type", equalTo("application/xml"))
                .withRequestBody(equalToXml("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                        "<user>" +
                        "<cellphone>3003003333</cellphone>" +
                        "<country_code>1</country_code>" + //Country defaults to +1
                        "<email>test@example.com</email>" +
                        "</user>")));
        Assert.assertEquals(1000, user.getId());
        Assert.assertTrue(user.isOk());
    }

    @Test
    public void testCreateUserErrorInvalid(){
        stubFor(post(urlPathEqualTo("/protected/xml/users/new"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/xml")
                        .withBody("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                "<errors>\n" +
                                "    <message>User was not valid</message>\n" +
                                "    <error-code>60027</error-code>\n" +
                                "</errors>")));

        final User user = client.createUser("test@example.com", "3001"); //Invalid Phone sent

        Assert.assertFalse(user.isOk());
        Assert.assertEquals(400, user.getStatus());
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
        Hash response = client.requestSms(Integer.parseInt(testUserId), map);

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
        Hash reponse = client.requestSms(badUserId, map);

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
        Hash response = client.requestSms(Integer.parseInt(testUserId));

        verify(getRequestedFor(urlPathEqualTo("/protected/xml/sms/" + testUserId))
                .withHeader("X-Authy-API-Key", equalTo(testApiKey)));
        // isOK() is the method that will allow you to know if the request worked.
        Assert.assertTrue(response.isOk());
        // there's also a response message.
        Assert.assertEquals("Ignored: SMS is not needed for smartphones. Pass force=true if you want to actually send it anyway.", response.getMessage());
    }


}
