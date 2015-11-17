package com.authy.api;

import junit.framework.Assert;
import org.junit.Test;

public class TestPhone {

  @Test
  public void testDefaultJsonValues() {
    Phone phone = new Phone();
    String result = phone.toJSON();
    String expected = "{\"locale\":\"en\"," +
      "\"verification_code\":\"Code no needed in this request, it is default value\"" +
      ",\"via\":\"sms\"}";
    Assert.assertEquals(result, expected);
  }

  @Test
  public void returnsJsonPhoneValuesStartVerification() {
    Phone phone = new Phone("555-555-5555", "1", "pt", "call");
    String result = phone.toJSON();
    String expected = "{" + 
      "\"country_code\":\"1\"" +
      ",\"phone_number\":\"555-555-5555\"" +
      ",\"locale\":\"pt\"," +
      "\"verification_code\":\"Code no needed in this request, it is default value\"" +
      ",\"via\":\"call\"" + 
      "}";
    Assert.assertEquals(result, expected);
  }

  @Test
  public void returnsJsonPhoneValuesCheckVerification() {
    Phone phone = new Phone("555-555-5555", "1", "9999");
    String result = phone.toJSON();
    String expected = "{" + 
      "\"country_code\":\"1\"" +
      ",\"phone_number\":\"555-555-5555\"" +
      ",\"locale\":\"en\"," +
      "\"verification_code\":\"9999\"" +
      ",\"via\":\"sms\"" + 
      "}";
    Assert.assertEquals(result, expected);
  }
}
