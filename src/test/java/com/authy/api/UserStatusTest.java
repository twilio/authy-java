package com.authy.api;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;

public class UserStatusTest {

    public static final int USER_ID = 1234;
    public static final String PHONE_NUMBER = "456 758 8990";
    public static final String DEVICE_A = "deviceA";
    public static final String DEVICE_B = "deviceB";
    private UserStatus userStatus;

    /**
     * Compare JSON string without enforcing the order
     */
    public void assertJsonEqualsNonStrict(String json1, String json2) {
        try {
            JSONAssert.assertEquals(json1, json2, false);
        } catch (JSONException jse) {
            throw new IllegalArgumentException(jse.getMessage());
        }
    }

    @Before
    public void setup() {
        userStatus = new UserStatus();
        userStatus.setStatus(200);
        userStatus.setUserId(USER_ID);
        userStatus.setSuccess(true);
        userStatus.setConfirmed(true);
        userStatus.setRegistered(true);
        userStatus.setCountryCode(1);
        userStatus.setPhoneNumber(PHONE_NUMBER);
        List<String> devices = new ArrayList<>();
        devices.add(DEVICE_A);
        devices.add(DEVICE_B);
        userStatus.setDevices(devices);
    }

    @Test
    public void testToMap() {
        Map<String, String> userStatusMap = userStatus.toMap();
        assertNotNull(userStatusMap);
        assertEquals(Integer.toString(USER_ID), userStatusMap.get("userId"));
        assertEquals(Boolean.toString(true), userStatusMap.get("success"));
        assertEquals(Boolean.toString(true), userStatusMap.get("confirmed"));
        assertEquals(Boolean.toString(true), userStatusMap.get("registered"));
        assertEquals(Integer.toString(1), userStatusMap.get("countryCode"));
        assertEquals(PHONE_NUMBER, userStatusMap.get("phoneNumber"));
        assertEquals(String.format("[%s, %s]", DEVICE_A, DEVICE_B), userStatusMap.get("devices"));
    }

    @Test
    public void testToXML() {
        String userStatusXml = userStatus.toXML();
        assertNotNull(userStatusXml);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                        "<user_status><status>200</status><userId>1234</userId><success>true</success><confirmed>true</confirmed>" +
                        "<registered>true</registered><country_code>1</country_code>" +
                        "<devices><device>deviceA</device><device>deviceB</device></devices>" +
                        "<phone_number>456 758 8990</phone_number></user_status>",
                userStatusXml);
    }

    @Test
    public void testToJSON() {
        String userStatusJson = userStatus.toJSON();
        assertNotNull(userStatusJson);
        assertJsonEqualsNonStrict(userStatusJson, "{\"phoneNumber\":\"456 758 8990\"," +
                "\"devices\":\"[deviceA, deviceB]\",\"success\":\"true\"," +
                "\"countryCode\":\"1\",\"registered\":\"true\",\"userId\":\"1234\",\"confirmed\":\"true\"}");
    }
}