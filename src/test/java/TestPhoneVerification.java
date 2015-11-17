package com.authy.api;

import junit.framework.Assert;
import org.junit.Test;

public class TestPhoneVerification {

  @Test
  public void itChecksContentTypeToBeJson() {
    PhoneVerification phoneVerification = new PhoneVerification("api.url", "api.key");
    Assert.assertEquals(phoneVerification.getContentType(), "application/json");
  }

}
