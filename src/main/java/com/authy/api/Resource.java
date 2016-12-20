package com.authy.api;

import com.authy.AuthyApiClient;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Class to send http requests.
 * @author Julian Camargo
 *
 */
public class Resource {
  public static final String ENCODE = "UTF-8";
  public static final String XML_CONTENT_TYPE = "application/xml";
  public static final String JSON_CONTENT_TYPE = "application/json";
  private String apiUri, apiKey;
  private int status;
  private boolean testFlag = false;
  private Map<String, String> defaultOptions;
  private boolean isJSON = false;
  private String contentType = XML_CONTENT_TYPE;

  public Resource(String uri, String key) {
    apiUri = uri;
    apiKey = key;
  }

  public Resource(String uri, String key, String contentType) {
    apiUri = uri;
    apiKey = key;
    isJSON = contentType == "JSON";
    if(isJSON)
      this.contentType = JSON_CONTENT_TYPE;
  }

  public Resource(String uri, String key, boolean testFlag) {
    apiUri = uri;
    apiKey = key;
    this.testFlag = testFlag;
  }

  public Resource(String uri, String key, boolean testFlag,  String contentType) {
    apiUri = uri;
    apiKey = key;
    this.testFlag = testFlag;
    isJSON = contentType == "JSON";
    if(isJSON)
      this.contentType = JSON_CONTENT_TYPE;
  }

  /**
   * POST method.
   * @param path
   * @param data
   * @return response from API.
   */
  public String post(String path, Formattable data) {
    return request("POST", path, data, getDefaultOptions());
  }

  /**
   * GET method.
   * @param path
   * @param data
   * @return response from API.
   */
  public String get(String path, Formattable data) {
    return request("GET", path, data, getDefaultOptions());
  }

  /**
   * PUT method.
   * @param path
   * @param data
   * @return response from API.
   */
  public String put(String path, Formattable data) {
    return request("PUT", path, data, getDefaultOptions());
  }

  /**
   * DELETE method.
   * @param path
   * @param data
   * @return response from API.
   */
  public String delete(String path, Formattable data) {
    return request("DELETE", path, data, getDefaultOptions());
  }

  public String request(String method, String path, Formattable data, Map<String, String> options) {
    HttpURLConnection connection = null;
    String answer = null;

    try {
      StringBuilder sb = new StringBuilder();

      if(method.equals("GET")) {
        sb.append(prepareGet(data));
      }

      URL url = new URL(apiUri + path + sb.toString());
      connection = createConnection(url, method, options);

      connection.setRequestProperty("X-Authy-API-Key", apiKey);

      if(method.equals("POST") || method.equals("PUT")) {
        if(isJSON) {
          writeJson(connection, data);
        } else {
          writeXml(connection, data);
        }
      }

      status = connection.getResponseCode();
      answer = getResponse(connection);
    }
    catch(SSLHandshakeException e) {
      System.err.println("SSL verification is failing. This might be because of an attack. Contact support@authy.com");
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    return answer;
  }

  /**
   * Get status from response.
   * @return status code.
   */
  public int getStatus() {
    return status;
  }

  public String getContentType() {
    return this.contentType;
  }

  protected HttpURLConnection createConnection(URL url, String method,
      Map<String, String> options) throws Exception {


    HttpURLConnection connection = null;
    if(testFlag)
      connection = (HttpURLConnection)url.openConnection();
    else
      connection = (HttpsURLConnection)url.openConnection();

    connection.setRequestMethod(method);

    for(Entry<String, String> s : options.entrySet()) {
      connection.setRequestProperty(s.getKey(), s.getValue());
    }

    connection.setDoOutput(true);

    return connection;
  }

  private String getResponse(HttpURLConnection connection) throws Exception {
    InputStream in = null;
    // Load stream
    if(status != 200) {
      in = connection.getErrorStream();
    }
    else {
      in = connection.getInputStream();
    }

    BufferedInputStream input = new BufferedInputStream(in);
    StringBuffer sb = new StringBuffer();
    int ch;

    while((ch = input.read()) != -1) {
      sb.append((char)ch);
    }
    input.close();

    return sb.toString();
  }

  private void writeXml(HttpURLConnection connection, Formattable data) throws SSLHandshakeException, IOException {
    if(data == null)
      return;

    OutputStream os = connection.getOutputStream();

    BufferedWriter output = new BufferedWriter(new OutputStreamWriter(os));
    output.write(data.toXML());
    output.flush();
    output.close();
  }

  private void writeJson(HttpURLConnection connection, Formattable data) throws SSLHandshakeException, IOException {
    if(data == null)
      return;

    OutputStream os = connection.getOutputStream();

    BufferedWriter output = new BufferedWriter(new OutputStreamWriter(os));
    output.write(data.toJSON());
    output.flush();
    output.close();
  }


  private String prepareGet(Formattable data) throws Exception {
    if(data == null)
      return "";

    StringBuffer sb = new StringBuffer();
    Map<String, String> params = data.toMap();

    for(Entry<String, String> s : params.entrySet()) {
      sb.append('&');
      sb.append(URLEncoder.encode(s.getKey(), ENCODE) + "=" + URLEncoder.encode(s.getValue(), ENCODE));
    }

    return sb.toString();
  }

  private Map<String, String> getDefaultOptions() {
    if(this.defaultOptions == null || this.defaultOptions.isEmpty()){
      this.defaultOptions = new HashMap<String, String>();
      this.defaultOptions.put("Content-Type", contentType);
      this.defaultOptions.put("Content-Type", contentType);
      this.defaultOptions.put("User-Agent", getUserAgent());
    }
    return this.defaultOptions;
  }

  private String getUserAgent() {
    String os = String.format("%s-%s-%s; Java %s", System.getProperty("os.name"), System.getProperty("os.version"),
        System.getProperty("os.arch"), System.getProperty("java.specification.version"));
    return String.format("%s/%s (%s)", AuthyApiClient.CLIENT_NAME, AuthyApiClient.VERSION, os);
  }
}
