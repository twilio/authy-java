package com.authy.api;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

import com.authy.AuthyApiClient;
import com.authy.AuthyException;
import com.authy.OneTouchException;
import com.authy.api.ApprovalRequestParams.Resolution;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit tests for the API described at: http://docs.authy.com/onetouch.html#onetouch-api
 *
 * @author hansospina
 * <p>
 * Copyright © 2017 Twilio, Inc. All Rights Reserved.
 */
public class TestOneTouch extends TestApiBase {

    final private String testUserId = "30144611";
    final private String testOneTouchUUID = "8d031630-d15b-0134-b0a8-0a77bbe8093e";
    final private String successStatusResponse = "{" +
            "    \"approval_request\": {" +
            "        \"_app_name\": \"testhans\"," +
            "        \"_app_serial_id\": 46113," +
            "        \"_authy_id\": 28894864," +
            "        \"_id\": \"589d12e07d558e6b91e9370a\"," +
            "        \"_user_email\": \"hans+1@allcode.com\"," +
            "        \"app_id\": \"58547f6f4014250a11c201f6\"," +
            "        \"created_at\": \"2017-02-10T01:09:52Z\"," +
            "        \"notified\": false," +
            "        \"processed_at\": \"2017-02-13T11:11:58Z\"," +
            "        \"seconds_to_expire\": 86400," +
            "        \"status\": \"expired\"," +
            "        \"updated_at\": \"2017-02-13T11:11:58Z\"," +
            "        \"user_id\": \"5840a9328aa2fb674c485436\"," +
            "        \"uuid\": \"8d031630-d15b-0134-b0a8-0a77bbe8093e\"" +
            "    }," +
            "    \"success\": true" +
            "}";

    final private String successSendApprovalRequestResponse = "{" +
            "    \"approval_request\": {" +
            "        \"uuid\": \"dda2c400-bc43-0135-d7be-1285ca17e122\"" +
            "    }," +
            "    \"success\": true" +
            "}";

    final private String userNotFoundResponse = "{"
            + "    \"message\": \"User not found.\","
            + "    \"success\": false,"
            + "    \"errors\": {"
            + "        \"message\": \"User not found.\""
            + "    },"
            + "    \"error_code\": \"60026\""
            + "}";

    final private String approvalRequestNotFound = "{"
            + "    \"message\": \"Approval request not found: 3f05a350-4b2c-0136-f779-12c0c2bf9easd\","
            + "    \"success\": false,"
            + "    \"errors\": {},"
            + "    \"error_code\": \"60049\""
            + "}";

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private AuthyApiClient client;

    @Before
    public void setUp() {
        client = new AuthyApiClient(testApiKey, testHost, true);
    }

    @Test
    public void testEmptyMessage() throws OneTouchException {
        thrown.expect(OneTouchException.class);
        thrown.expectMessage(ApprovalRequestParams.Builder.MESSAGE_ERROR);

        ApprovalRequestParams approvalRequestParams = new ApprovalRequestParams.Builder(Integer.parseInt(testUserId), "")
                .addDetail("username", "User")
                .addDetail("location", "California,USA")
                .addHiddenDetail("ip_address", "10.10.3.203")
                .addLogo(Resolution.Default, "http://image.co").build();

        Assert.fail();
    }

    @Test
    public void testAuthNull() throws OneTouchException {
        thrown.expect(OneTouchException.class);
        thrown.expectMessage(ApprovalRequestParams.Builder.AUTHYID_ERROR);

        ApprovalRequestParams approvalRequestParams = new ApprovalRequestParams.Builder(null, "Authorize OneTouch Unit Test")
                .addDetail("username", "User")
                .addDetail("location", "California,USA")
                .addHiddenDetail("ip_address", "10.10.3.203")
                .addLogo(Resolution.Default, "http://image.co")
                .build();

        Assert.fail();
    }

    @Test
    public void testSendApprovalRequestOk() throws AuthyException {
        stubFor(post(urlPathEqualTo("/onetouch/json/users/" + testUserId + "/approval_requests"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=utf-8")
                        .withBody(successSendApprovalRequestResponse)));

        ApprovalRequestParams approvalRequestParams = new ApprovalRequestParams.Builder(Integer.parseInt(testUserId), "Authorize OneTouch Unit Test")
                .addDetail("username", "User")
                .addDetail("location", "California,USA")
                .addHiddenDetail("ip_address", "10.10.3.203")
                .addLogo(Resolution.Low, "http://image.low")
                .addLogo(Resolution.Medium, "http://image.co")
                .addLogo(Resolution.Default, "http://image.co")
                .build();

        OneTouchResponse response = client.getOneTouch().sendApprovalRequest(approvalRequestParams);

        verify(postRequestedFor(urlPathEqualTo("/onetouch/json/users/" + testUserId + "/approval_requests"))
                .withHeader("X-Authy-API-Key", equalTo(testApiKey))
                .withRequestBody(equalToJson("{" +
                        "  \"hidden_details\" : {" +
                        "    \"ip_address\" : \"10.10.3.203\"" +
                        "  }," +
                        "  \"details\" : {" +
                        "    \"location\" : \"California,USA\"," +
                        "    \"username\" : \"User\"" +
                        "  }," +
                        "  \"message\" : \"Authorize OneTouch Unit Test\"," +
                        "  \"logos\" : [ {" +
                        "    \"res\" : \"med\"," +
                        "    \"url\" : \"http://image.co\"" +
                        "  }, {" +
                        "    \"res\" : \"low\"," +
                        "    \"url\" : \"http://image.low\"" +
                        "  }, {" +
                        "    \"res\" : \"default\"," +
                        "    \"url\" : \"http://image.co\"" +
                        "  } ]" +
                        "}", true, true)));
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testBadDetail() throws OneTouchException {
        thrown.expect(OneTouchException.class);
        thrown.expectMessage(ApprovalRequestParams.Builder.DETAIL_ERROR);

        ApprovalRequestParams approvalRequestParams = new ApprovalRequestParams.Builder(Integer.parseInt(testUserId), "Authorize OneTouch Unit Test")
                .addDetail("", "")
                .addHiddenDetail("ip_address", "10.10.3.203")
                .addLogo(Resolution.Default, "http://image.co")
                .build();

        Assert.fail();
    }

    @Test
    public void testHiddenDetail() throws OneTouchException {
        thrown.expect(OneTouchException.class);
        thrown.expectMessage(ApprovalRequestParams.Builder.HIDDEN_DETAIL_ERROR);

        ApprovalRequestParams approvalRequestParams = new ApprovalRequestParams.Builder(Integer.parseInt(testUserId), "Authorize OneTouch Unit Test")
                .addDetail("username", "User")
                .addDetail("location", "California,USA")
                .addHiddenDetail("ip_address", null)
                .addLogo(Resolution.Default, "http://image.co")
                .build();

        Assert.fail();
    }

    @Test
    public void testDefaultLogo() throws OneTouchException {
        thrown.expect(OneTouchException.class);
        thrown.expectMessage(ApprovalRequestParams.Builder.LOGO_ERROR_DEFAULT);

        ApprovalRequestParams approvalRequestParams = new ApprovalRequestParams.Builder(Integer.parseInt(testUserId), "Authorize OneTouch Unit Test")
                .addDetail("username", "User")
                .addDetail("location", "California,USA")
                .addLogo(Resolution.Low, "http://image.co")
                .build();

        Assert.fail();
    }


    @Test
    public void testGetApprovalRequestStatus() throws Exception {
        stubFor(get(urlPathEqualTo("/onetouch/json/approval_requests/" + testOneTouchUUID))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=utf-8")
                        .withBody(successStatusResponse)));

        OneTouchResponse response = client.getOneTouch().getApprovalRequestStatus(testOneTouchUUID);

        verify(getRequestedFor(urlPathEqualTo("/onetouch/json/approval_requests/" + testOneTouchUUID))
                .withHeader("X-Authy-API-Key", equalTo(testApiKey)));
        Assert.assertTrue(response.isSuccess());
        Assert.assertNotNull(response.getApprovalRequest().getStatus());
    }

    @Test
    public void testUserNotFoundError() throws Exception {
        String invalidUserId = "12342325";
        stubFor(post(urlPathEqualTo("/onetouch/json/users/" + invalidUserId + "/approval_requests"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json;charset=utf-8")
                        .withBody(userNotFoundResponse)));

        ApprovalRequestParams approvalRequestParams = new ApprovalRequestParams.Builder(Integer.parseInt(invalidUserId), "Authorize OneTouch Unit Test")
                .addDetail("username", "User")
                .addDetail("location", "California,USA")
                .addHiddenDetail("ip_address", "10.10.3.203")
                .addLogo(Resolution.Low, "http://image.low")
                .addLogo(Resolution.Medium, "http://image.co")
                .addLogo(Resolution.Default, "http://image.co")
                .build();

        OneTouchResponse response = client.getOneTouch().sendApprovalRequest(approvalRequestParams);

        assertFalse(response.isSuccess());
        assertEquals(60026, response.getError().getCode().intValue());
    }

    @Test
    public void testApprovalRequestNotFoundError() throws Exception {
        String invalidOneTouchUUID = "3f05a350-4b2c-0136-f779-12c0c2bf9easd";
        stubFor(get(urlPathEqualTo("/onetouch/json/approval_requests/" + invalidOneTouchUUID))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json;charset=utf-8")
                        .withBody(approvalRequestNotFound)));

        OneTouchResponse response = client.getOneTouch().getApprovalRequestStatus(invalidOneTouchUUID);

        assertFalse(response.isSuccess());
        assertEquals(60049, response.getError().getCode().intValue());
        assertEquals("60049", response.getErrorCode());
    }
}
