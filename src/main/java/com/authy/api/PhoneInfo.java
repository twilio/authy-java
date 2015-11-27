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
public class PhoneInfo extends Resource {
  public static final String PHONE_INFO_API_PATH = "/protected/json/phones/";

  public PhoneInfo(String uri, String key) {
    super(uri, key, "JSON");
  }

  public PhoneInfo(String uri, String key, boolean testFlag) {
    super(uri, key, testFlag, "JSON");
  }

  public PhoneInfoResponse info(String phoneNumber, String countryCode) {
    Params params = new Params();
    params.setAttribute("phone_number", phoneNumber);
    params.setAttribute("country_code", countryCode);
    return getInfo(params);
  }

  public PhoneInfoResponse info(String phoneNumber, String countryCode, Params params) {
    params.setAttribute("phone_number", phoneNumber);
    params.setAttribute("country_code", countryCode);
    return getInfo(params);
  }

  private PhoneInfoResponse getInfo(Params params) {
    PhoneInfoResponse info = new PhoneInfoResponse();
    StringBuffer path = new StringBuffer(PHONE_INFO_API_PATH);
    String response = "";

    try {
      path.append("info");
      response = this.get(path.toString(), params);

      info.setStatus(this.getStatus());
      info.setResponse(response);
    }

    catch(Exception e) {
      e.printStackTrace();
    }
    return info;
  }

}
