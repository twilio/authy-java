package com.authy.api;

import com.authy.AuthyApiClient;
import com.authy.AuthyException;

import org.hamcrest.core.SubstringMatcher;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class TestUsers extends TestApiBase {

    private Users client;
    final private String testUserId = "30144611";

    private final String successResponseForced = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<hash>" +
            "    <success type=\"boolean\">true</success>" +
            "    <message>SMS token was sent</message>" +
            "    <cellphone>+57-XXX-XXX-XX12</cellphone>" +
            "</hash>";

    private final String userNotFoundResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<errors>" +
            "    <message>User not found.</message>" +
            "    <error-code>60026</error-code>" +
            "</errors>";

    private final String successResponseNotForced = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<hash>" +
            "    <message>Ignored: SMS is not needed for smartphones. Pass force=true if you want to actually send it anyway.</message>" +
            "    <cellphone>+57-XXX-XXX-XX12</cellphone>" +
            "    <device>android</device>" +
            "    <ignored type=\"boolean\">true</ignored>" +
            "    <success type=\"boolean\">true</success>" +
            "</hash>";

    private final String successCreateUserResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<hash>" +
            "    <message>User created successfully.</message>" +
            "    <user>" +
            "        <id type=\"integer\">1000</id>" +
            "    </user>" +
            "    <success type=\"boolean\">true</success>" +
            "</hash>"; ;

    @Before
    public void setUp() {
        client = new AuthyApiClient(testApiKey, testHost, true).getUsers();
    }

    @Test
    public void testCreateUser() throws AuthyException {
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
        assertEquals(1000, user.getId());
        assertTrue(user.isOk());
    }

    @Test
    public void testCreateUserDefaultCountry() throws AuthyException {
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
        assertEquals(1000, user.getId());
        assertTrue(user.isOk());
    }

    @Test
    public void testCreateUserErrorInvalid() throws AuthyException {
        stubFor(post(urlPathEqualTo("/protected/xml/users/new"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/xml")
                        .withBody("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                                "<errors>" +
                                "    <message>User was not valid</message>" +
                                "    <error-code>60027</error-code>" +
                                "</errors>")));

        final User user = client.createUser("test@example.com", "3001"); //Invalid Phone sent

        assertFalse(user.isOk());
        assertEquals(400, user.getStatus());
    }

    @Test
    public void testRequestSMS() throws AuthyException {
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
        assertTrue(response.isOk());
        // there's also a response message.
        assertEquals("SMS token was sent", response.getMessage());
    }

    @Test
    public void testUserNotFoundSMS() throws AuthyException {
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
        assertFalse(reponse.isOk());
    }

    @Test
    public void testRequestSMSNoForce() throws AuthyException {
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
        assertTrue(response.isOk());
        // there's also a response message.
        assertEquals("Ignored: SMS is not needed for smartphones. Pass force=true if you want to actually send it anyway.", response.getMessage());
    }

    @Test
    public void testRequestCall() throws AuthyException {
        stubFor(get(urlPathEqualTo("/protected/xml/call/" + testUserId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/xml")
                        .withBody("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                                "<hash>" +
                                "    <message>Call ignored. User is using  App Tokens and this call is not necessary. Pass force=true if you still want to call users that are using the App.</message>" +
                                "    <cellphone>+1-XXX-XXX-XX12</cellphone>" +
                                "    <device nil=\"true\"/>" +
                                "    <ignored type=\"boolean\">true</ignored>" +
                                "    <success type=\"boolean\">true</success>" +
                                "</hash>")));

        Hash response  = client.requestCall(Integer.parseInt(testUserId));

        verify(getRequestedFor(urlPathEqualTo("/protected/xml/call/" + testUserId))
                .withHeader("X-Authy-API-Key", equalTo(testApiKey)));
        assertTrue(response.isOk());
        assertThat(response.getMessage(), containsString("Call ignored. User is using  App Tokens and this call is not necessary."));
    }

    @Test
    public void testRemoveUser() throws AuthyException {
        stubFor(post(urlPathEqualTo("/protected/xml/users/delete/" + testUserId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/xml")
                        .withBody("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                                "<hash>" +
                                "    <message>User removed from application</message>" +
                                "    <success type=\"boolean\">true</success>" +
                                "</hash>")));

        Hash response = client.deleteUser(Integer.parseInt(testUserId));

        verify(postRequestedFor(urlPathEqualTo("/protected/xml/users/delete/" + testUserId))
                .withHeader("Content-Type", equalTo("application/xml")) //TODO: this Content-Type even if it works doesn't seem like the more appropriate
                .withHeader("X-Authy-API-Key", equalTo(testApiKey))
                .withRequestBody(equalTo("")));
        assertTrue(response.isOk());
        assertThat(response.getMessage(), containsString("User removed from application"));
    }

    @Test
    public void testRemoveUserErrorNotFound() throws AuthyException {
        stubFor(post(urlPathEqualTo("/protected/xml/users/delete/" + testUserId))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/xml")
                        .withBody("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                                "<errors>" +
                                "    <error-code>60026</error-code>" +
                                "    <message>User doesn't exist</message>" +
                                "    <errors>" +
                                "        <message>User doesn't exist</message>" +
                                "    </errors>" +
                                "    <success type=\"boolean\">false</success>" +
                                "</errors>")));

        Hash response = client.deleteUser(Integer.parseInt(testUserId));

        assertFalse(response.isOk());
        assertThat(response.getStatus(), is(404));
        assertThat(response.getError().getMessage(), containsString("User doesn't exist"));
    }
}
