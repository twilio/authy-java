package com.authy.api;

import com.authy.AuthyApiClient;
import com.authy.OneTouchException;
import com.authy.api.ApprovalRequestParams.Resolution;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Unit tests for the API described at: http://docs.authy.com/onetouch.html#onetouch-api
 *
 * @author hansospina
 *         <p>
 *         Copyright © 2017 Twilio, Inc. All Rights Reserved.
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

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private AuthyApiClient client;

    @Before
    public void setUp() throws IOException {
        // Let's configure the API Client with the properties defined at the test.properties file.
        Assert.assertNotNull(properties.getProperty("api_key"));
        Assert.assertNotNull(properties.getProperty("api_url"));
        client = new AuthyApiClient(properties.getProperty("api_key"), properties.getProperty("api_url"), true);
    }

    @Test
    public void testEmptyMessage() throws OneTouchException {
        //Check that the properties file has a valid
        thrown.expect(OneTouchException.class);
        thrown.expectMessage(ApprovalRequestParams.Builder.MESSAGE_ERROR);
        Assert.assertNotNull(properties.getProperty("user_id"));
        ApprovalRequestParams approvalRequestParams = new ApprovalRequestParams.Builder(Integer.parseInt(properties.getProperty("user_id")), "")
                .addDetail("username", "User")
                .addDetail("location", "California,USA")
                .addHiddenDetail("ip_address", "10.10.3.203")
                .addLogo(Resolution.Default, "http://image.co").build();

        OneTouchResponse response = client.getOneTouch().sendApprovalRequest(approvalRequestParams);
        Assert.assertTrue(response.isSuccess());


    }

    @Test
    public void testAuthNull() throws OneTouchException {
        //Check that the properties file has a valid
        thrown.expect(OneTouchException.class);
        thrown.expectMessage(ApprovalRequestParams.Builder.AUTHYID_ERROR);
        Assert.assertNotNull(properties.getProperty("user_id"));
        ApprovalRequestParams approvalRequestParams = new ApprovalRequestParams.Builder(null, "Authorize OneTouch Unit Test")
                .addDetail("username", "User")
                .addDetail("location", "California,USA")
                .addHiddenDetail("ip_address", "10.10.3.203")
                .addLogo(Resolution.Default, "http://image.co")
                .build();
        OneTouchResponse response = client.getOneTouch().sendApprovalRequest(approvalRequestParams);

        // the test case should never reach this point
        Assert.assertTrue(false);


    }

    @Test
    public void testSendApprovalRequestOk() throws OneTouchException {
        //Check that the properties file has a valid

        Assert.assertNotNull(properties.getProperty("user_id"));
        ApprovalRequestParams approvalRequestParams = new ApprovalRequestParams.Builder(Integer.parseInt(properties.getProperty("user_id")), "Authorize OneTouch Unit Test")
                .addDetail("username", "User")
                .addDetail("location", "California,USA")
                .addHiddenDetail("ip_address", "10.10.3.203")
                .addLogo(Resolution.Low, "http://image.co")
                .addLogo(Resolution.Medium, "http://image.co")
                .addLogo(Resolution.Default, "http://image.co")
                .build();

        OneTouchResponse response = client.getOneTouch().sendApprovalRequest(approvalRequestParams);

        // the test case should never reach this point
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testBadDetail() throws OneTouchException {
        //Check that the properties file has a valid
        thrown.expect(OneTouchException.class);
        thrown.expectMessage(ApprovalRequestParams.Builder.DETAIL_ERROR);
        Assert.assertNotNull(properties.getProperty("user_id"));
        ApprovalRequestParams approvalRequestParams = new ApprovalRequestParams.Builder(Integer.parseInt(properties.getProperty("user_id")), "Authorize OneTouch Unit Test")
                .addDetail("", "")
                .addHiddenDetail("ip_address", "10.10.3.203")
                .addLogo(Resolution.Default, "http://image.co")
                .build();

        OneTouchResponse response = client.getOneTouch().sendApprovalRequest(approvalRequestParams);

        // the test case should never reach this point
        Assert.assertTrue(false);
    }

    @Test
    public void testHiddenDetail() throws OneTouchException {
        //Check that the properties file has a valid
        thrown.expect(OneTouchException.class);
        thrown.expectMessage(ApprovalRequestParams.Builder.HIDDEN_DETAIL_ERROR);
        Assert.assertNotNull(properties.getProperty("user_id"));
        ApprovalRequestParams approvalRequestParams = new ApprovalRequestParams.Builder(Integer.parseInt(properties.getProperty("user_id")), "Authorize OneTouch Unit Test")
                .addDetail("username", "User")
                .addDetail("location", "California,USA")
                .addHiddenDetail("ip_address", null)
                .addLogo(Resolution.Default, "http://image.co")
                .build();

        OneTouchResponse response = client.getOneTouch().sendApprovalRequest(approvalRequestParams);

        // the test case should never reach this point
        Assert.assertTrue(false);
    }

    @Test
    public void testDefaultLogo() throws OneTouchException {
        //Check that the properties file has a valid
        thrown.expect(OneTouchException.class);
        thrown.expectMessage(ApprovalRequestParams.Builder.LOGO_ERROR_DEFAULT);
        Assert.assertNotNull(properties.getProperty("user_id"));
        ApprovalRequestParams approvalRequestParams = new ApprovalRequestParams.Builder(Integer.parseInt(properties.getProperty("user_id")), "Authorize OneTouch Unit Test")
                .addDetail("username", "User")
                .addDetail("location", "California,USA")
                .addLogo(Resolution.Low, "http://image.co")
                .build();

        OneTouchResponse response = client.getOneTouch().sendApprovalRequest(approvalRequestParams);

        // the test case should never reach this point
        Assert.assertTrue(false);
    }


    @Test
    public void testGetApprovalRequestStatus() throws Exception {
        //Check that the properties file has a valid
        Assert.assertNotNull(properties.getProperty("onetouch_uuid"));

        OneTouchResponse response = client.getOneTouch().getApprovalRequestStatus(properties.getProperty("onetouch_uuid"));
        Assert.assertTrue(response.isSuccess());
        Assert.assertNotNull(response.getApprovalRequest().getStatus());

    }

}
