package com.authy.api;

import com.authy.AuthyApiClient;
import com.authy.AuthyException;

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

    private final String successResponseForced = "{" +
            "    \"success\": true," +
            "    \"message\": \"SMS token was sent\"," +
            "    \"cellphone\": \"+57-XXX-XXX-XX12\"" +
            "}";

    private final String userNotFoundResponse = "{" +
            "    \"message\": \"User not found.\"," +
            "    \"success\": false," +
            "    \"errors\": {" +
            "        \"message\": \"User not found.\"" +
            "    }," +
            "    \"error_code\": \"60026\"" +
            "}";

    private final String successResponseNotForced = "{" +
            "    \"message\": \"Ignored: SMS is not needed for smartphones. Pass force=true if you want to actually send it anyway.\"," +
            "    \"cellphone\": \"+57-XXX-XXX-XX12\"," +
            "    \"device\": \"android\"," +
            "    \"ignored\": true," +
            "    \"success\": true" +
            "}";

    private final String successCreateUserResponse = "{\"message\":\"User created successfully.\",\"user\":{\"id\":1000},\"success\":true}";

    @Before
    public void setUp() {
        client = new AuthyApiClient(testApiKey, testHost, true).getUsers();
    }

    @Test
    public void testCreateUser() throws AuthyException {
        stubFor(post(urlPathEqualTo("/protected/json/users/new"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(successCreateUserResponse)));

        final User user = client.createUser("test@example.com", "3003003333", "57");

        verify(postRequestedFor(urlPathEqualTo("/protected/json/users/new"))
                .withHeader("X-Authy-API-Key", equalTo(testApiKey))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(equalToJson("{" +
                        "\"user\": {"
                        + "  \"country_code\" : \"57\","
                        + "  \"cellphone\" : \"3003003333\","
                        + "  \"email\" : \"test@example.com\""
                        + "}}")));
        assertEquals(1000, user.getId());
        assertTrue(user.isOk());
    }

    @Test
    public void testCreateUserDefaultCountry() throws AuthyException {
        stubFor(post(urlPathEqualTo("/protected/json/users/new"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(successCreateUserResponse)));

        final User user = client.createUser("test@example.com", "3003003333");

        verify(postRequestedFor(urlPathEqualTo("/protected/json/users/new"))
                .withHeader("X-Authy-API-Key", equalTo(testApiKey))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(equalToJson("{" +
                        "\"user\": {"
                        + "  \"country_code\" : \"1\","
                        + "  \"cellphone\" : \"3003003333\","
                        + "  \"email\" : \"test@example.com\""
                        + "}}")));
        assertEquals(1000, user.getId());
        assertTrue(user.isOk());
    }

    @Test
    public void testCreateUserErrorInvalid() throws AuthyException {
        stubFor(post(urlPathEqualTo("/protected/json/users/new"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{" +
                                "    \"message\": \"User was not valid\"," +
                                "    \"success\": false," +
                                "    \"errors\": {" +
                                "        \"cellphone\": \"is invalid\"," +
                                "        \"message\": \"User was not valid\"" +
                                "    }," +
                                "    \"cellphone\": \"is invalid\"," +
                                "    \"error_code\": \"60027\"" +
                                "}")));

        final User user = client.createUser("test@example.com", "3001"); //Invalid Phone sent

        assertFalse(user.isOk());
        assertEquals(400, user.getStatus());
        final Error error = user.getError();
        assertEquals("User was not valid", error.getMessage());
        assertEquals(60027, error.getCode().intValue());
    }

    @Test
    public void testRequestSMS() throws AuthyException {
        stubFor(get(urlPathEqualTo("/protected/json/sms/" + testUserId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(successResponseForced)));

        // let's setup some extra parameters
        HashMap<String, String> map = new HashMap<>();
        // We are testing SMS here so let's add the force param to have Authy send the SMS even if the
        // user has the Authy App installed
        map.put("force", "true");
        // This is the API normal call you will do to send an SMS, if we don't pass the force option authy will be
        // smart enough to decide if it sends the sms or just notifies the user inside the app
        Hash response = client.requestSms(Integer.parseInt(testUserId), map);

        verify(getRequestedFor(urlPathEqualTo("/protected/json/sms/" + testUserId))
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
        stubFor(get(urlPathEqualTo("/protected/json/sms/" + badUserId))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody(userNotFoundResponse)));

        // let's setup some extra parameters
        HashMap<String, String> map = new HashMap<>();
        // We are testing SMS here so let's add the force param to have Authy send the SMS even if the
        // user has the Authy App installed
        map.put("force", "true");
        // This is the API normal call you will do to send an SMS, if we don't pass the force option authy will be
        // smart enough to decide if it sends the sms or just notifies the user inside the app
        Hash reponse = client.requestSms(badUserId, map);

        verify(getRequestedFor(urlPathEqualTo("/protected/json/sms/" + badUserId))
                .withHeader("X-Authy-API-Key", equalTo(testApiKey))
                .withQueryParam("force", equalTo("true")));
        // isOK() is the method that will allow you to know if the request worked.
        assertFalse(reponse.isOk());
        final Error error = reponse.getError();
        assertNotNull(error);
        assertEquals(60026, error.getCode().intValue());
    }

    @Test
    public void testRequestSMSNoForce() throws AuthyException {
        stubFor(get(urlPathEqualTo("/protected/json/sms/" + testUserId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(successResponseNotForced)));

        // This is the API normal call you will do to send an SMS, if we don't pass the force option authy will be
        // smart enough to decide if it sends the sms or just notifies the user inside the app
        Hash response = client.requestSms(Integer.parseInt(testUserId));

        verify(getRequestedFor(urlPathEqualTo("/protected/json/sms/" + testUserId))
                .withHeader("X-Authy-API-Key", equalTo(testApiKey)));
        // isOK() is the method that will allow you to know if the request worked.
        assertTrue(response.isOk());
        // there's also a response message.
        assertEquals("Ignored: SMS is not needed for smartphones. Pass force=true if you want to actually send it anyway.", response.getMessage());
    }

    @Test
    public void testRequestCall() throws AuthyException {
        stubFor(get(urlPathEqualTo("/protected/json/call/" + testUserId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/xml")
                        .withBody("{" +
                                "    \"message\": \"Call ignored. User is using  App Tokens and this call is not necessary. Pass force=true if you still want to call users that are using the App.\","
                                +
                                "    \"cellphone\": \"+57-XXX-XXX-XX12\"," +
                                "    \"device\": \"android\"," +
                                "    \"ignored\": true," +
                                "    \"success\": true" +
                                "}")));

        Hash response = client.requestCall(Integer.parseInt(testUserId));

        verify(getRequestedFor(urlPathEqualTo("/protected/json/call/" + testUserId))
                .withHeader("X-Authy-API-Key", equalTo(testApiKey)));
        assertTrue(response.isOk());
        assertThat(response.getMessage(), containsString("Call ignored. User is using  App Tokens and this call is not necessary."));
    }

    @Test
    public void testRemoveUser() throws AuthyException {
        stubFor(post(urlPathEqualTo("/protected/json/users/delete/" + testUserId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{" +
                                "    \"message\": \"User removed from application\"," +
                                "    \"success\": true" +
                                "}")));

        Hash response = client.deleteUser(Integer.parseInt(testUserId));

        verify(postRequestedFor(urlPathEqualTo("/protected/json/users/delete/" + testUserId))
                .withHeader("Content-Type", equalTo("application/json")) //TODO: this Content-Type even if it works doesn't seem like the more appropriate
                .withHeader("X-Authy-API-Key", equalTo(testApiKey))
                .withRequestBody(equalTo("")));
        assertTrue(response.isOk());
        assertThat(response.getMessage(), containsString("User removed from application"));
    }

    @Test
    public void testRemoveUserErrorNotFound() throws AuthyException {
        stubFor(post(urlPathEqualTo("/protected/json/users/delete/" + testUserId))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody(userNotFoundResponse)));

        Hash response = client.deleteUser(Integer.parseInt(testUserId));

        assertFalse(response.isOk());
        assertThat(response.getStatus(), is(404));
        final Error error = response.getError();
        assertThat(error.getMessage(), containsString("User not found"));
        assertEquals(60026, error.getCode().intValue());
    }

    @Test
    public void testUserToXML() throws Exception {
        Users.User user = new Users.User("fake@email.com", "12345678", "1");
        String xml = user.toXML();

        assertEquals(xml, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><user><cellphone>12345678</cellphone><country_code>1</country_code><email>fake@email.com</email></user>");
    }
}
