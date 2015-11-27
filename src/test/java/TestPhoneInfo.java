package com.authy.api;

import junit.framework.Assert;
import org.junit.Test;

public class TestPhoneInfo {
  String apiKey = "bf12974d70818a08199d17d5e2bae630";
  String apiEndPoint = "http://sandbox-api.authy.com";
  PhoneInfo subject = new PhoneInfo(apiEndPoint, apiKey, true);

  @Test
  public void itTestsPhoneInfo() {
    PhoneInfoResponse result = subject.info("7754615609", "1");

    Assert.assertEquals(true, result.getMessage().contains("Phone number information as of"));
    Assert.assertEquals("", result.getProvider());
    Assert.assertEquals("landline", result.getType());
    Assert.assertEquals("false", result.getIsPorted());
    Assert.assertEquals("true", result.getSuccess());
  }

}
