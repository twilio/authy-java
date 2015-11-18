package com.authy.api;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.*;

/**
 *
 * @author Moisés Vargas
 *
 */
@XmlRootElement(name="verification")
public class Phone implements Response {
  String number;
  String countryCode;
  String locale = "en";
  String via = "sms";
  String verificationCode = "Code no needed in this request, it is default value";

  public Phone() {}

  public Phone(String number, String countryCode, String locale, String via) {
    this.number = number;
    this.countryCode = countryCode;
    this.locale = locale;
    this.via = via;
  }

  public Phone(String number, String countryCode, String verificationCode) {
    this.number = number;
    this.countryCode = countryCode;
    this.verificationCode = verificationCode;
  }

  public Phone(String number, String countryCode) {
    this.number = number;
    this.countryCode = countryCode;
  }

  @XmlElement(name="phone_number")
    public String getNumber() {
      return number;
  }

  @XmlElement(name="country_code")
    public String getCountryCode() {
      return countryCode;
  }

  @XmlElement(name="locale")
    public String getLocale() {
      return locale;
  }

  @XmlElement(name="via")
    public String getVia() {
      return via;
  }

  @XmlElement(name="verification_code")
    public String getVerificationCode() {
      return verificationCode;
  }

  /**
   * Map a Token instance to its XML representation.
   * @return a String with the description of this object in XML.
   */
  public String toXML() {
    StringWriter sw = new StringWriter();
    String xml = "";
    try {
      JAXBContext context = JAXBContext.newInstance(this.getClass());
      Marshaller marshaller = context.createMarshaller();

      marshaller.marshal(this, sw);
      xml = sw.toString();
    }catch(Exception e) {
      e.printStackTrace();
    }

    return xml.toString();
  }

  /**
   * Map a Token instance to its Java's Map representation.
   * @return a Java's Map with the description of this object.
   */
  public Map<String, String> toMap() {
    Map<String, String> map = new HashMap<String, String>();

    map.put("phone_number", number);
    map.put("country_code", countryCode);
    map.put("locale", locale);
    map.put("via", via);
    map.put("verification_code", verificationCode);

    return map;
  }

  public String toJSON(){
    org.json.JSONObject verification = new org.json.JSONObject();

    verification.put("phone_number", this.number);
    verification.put("country_code", this.countryCode);
    verification.put("locale", this.locale);
    verification.put("via", this.via);
    verification.put("verification_code", this.verificationCode);

    return verification.toString();
  }

}
