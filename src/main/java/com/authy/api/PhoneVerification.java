package com.authy.api;

import com.authy.AuthyException;

/**
 *
 * @author Authy Inc
 *
 */
public class PhoneVerification extends Resource {
  public static final String PHONE_VERIFICATION_API_PATH = "/protected/json/phones/verification/";

  public PhoneVerification(String uri, String key) {
    super(uri, key, "JSON");
  }

  public PhoneVerification(String uri, String key, boolean testFlag) {
    super(uri, key, testFlag, "JSON");
  }

  public Verification start(String phoneNumber, String countryCode, String via, Params params) {
    params.setAttribute("phone_number", phoneNumber);
    params.setAttribute("country_code", countryCode);
    params.setAttribute("via", via);

    Verification verification = new Verification();
    StringBuffer path = new StringBuffer(PHONE_VERIFICATION_API_PATH);
    String response = "";

    try {
      path.append("start");
      response = this.post(path.toString(), params);

      verification.setStatus(this.getStatus());
      verification.setResponse(response);
    }

    catch(Exception e) {
      e.printStackTrace();
    }
    return verification;
  }

  public Verification check(String phoneNumber, String countryCode, String code) {
    Params params = new Params();
    params.setAttribute("phone_number", phoneNumber);
    params.setAttribute("country_code", countryCode);
    params.setAttribute("verification_code", code);

    return verificationCheck(params);
  }

  public Verification check(String phoneNumber, String countryCode, String code, Params params) {
    params.setAttribute("phone_number", phoneNumber);
    params.setAttribute("country_code", countryCode);
    params.setAttribute("verification_code", code);

    return verificationCheck(params);
  }

  private Verification verificationCheck(Params params){
    Verification verification = new Verification();
    StringBuffer path = new StringBuffer(PHONE_VERIFICATION_API_PATH);
    String response = "";

    try {
      path.append("check");
      response = this.get(path.toString(), params);

      verification.setStatus(this.getStatus());
      verification.setResponse(response);
    }

    catch(Exception e) {
      e.printStackTrace();
    }
    return verification;
  }

}
