package com.authy.api;

import java.io.StringReader;
import java.net.URLEncoder;
import java.util.Map;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import com.authy.AuthyException;

/**
 *
 * @author Moisés Vargas
 *
 */
public class PhoneVerification extends Resource {
  public static final String START_VERIFICATION_CODE_PATH = "/protected/json/phones/verification/";

  public PhoneVerification(String uri, String key) {
    super(uri, key, "JSON");
  }

  public PhoneVerification(String uri, String key, boolean testFlag) {
    super(uri, key, testFlag, "JSON");
  }

  public Verification start(Phone phone) {
    Verification verification = new Verification();
    StringBuffer path = new StringBuffer(START_VERIFICATION_CODE_PATH);
    String response = "";

    try {
      path.append("start");
      response = this.post(path.toString(), phone);

      verification.setStatus(this.getStatus());
      verification.setResponse(response);
    }

    catch(Exception e) {
      e.printStackTrace();
    }
    return verification;
  }

}
