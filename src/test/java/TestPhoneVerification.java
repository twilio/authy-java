package com.authy.api;

import junit.framework.Assert;
import org.junit.Test;

public class TestPhoneVerification {
  String apiKey = "bf12974d70818a08199d17d5e2bae630";
  String apiEndPoint = "http://sandbox-api.authy.com";
  PhoneVerification subject = new PhoneVerification(apiEndPoint, apiKey, true);

  @Test
  public void itChecksContentTypeToBeJson() {
    PhoneVerification phoneVerification = new PhoneVerification("api.url", "api.key");
    Assert.assertEquals("application/json", phoneVerification.getContentType());
  }

  @Test
  public void itTestsVerificationStartEs() {
    Phone phone = new Phone("555-555-5555", "1", "es", "call");
    Verification result = subject.start(phone);
    Assert.assertEquals("Llamada a +1 555-555-5555 fue iniciada.", result.getMessage());
    Assert.assertEquals("true", result.getSuccess());
  }

  @Test
  public void itTestsVerificationStartEn() {
    Phone phone = new Phone("555-555-5555", "1", "en", "sms");
    Verification result = subject.start(phone);
    Assert.assertEquals("Text message sent to +1 555-555-5555.", result.getMessage());
    Assert.assertEquals("true", result.getSuccess());
  }

  @Test
  public void itTestsVerificationStartEnInvalid() {
    Phone phone = new Phone("282-23", "1", "en", "sms");
    Verification result = subject.start(phone);
    Assert.assertEquals("Phone verification couldn't be created: Phone number is mandatory, Country code is mandatory", result.getMessage());
    Assert.assertEquals("false", result.getSuccess());
  }

  @Test
  public void itTestsVerificationCheckNotFound() {
    Phone phone = new Phone("282-23", "1", "2061");
    Verification result = subject.check(phone);
    Assert.assertEquals("No pending verifications for +1 2-8223 found.", result.getMessage());
    Assert.assertEquals("false", result.getSuccess());
  }

}
