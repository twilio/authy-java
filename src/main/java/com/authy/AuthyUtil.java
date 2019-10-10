package com.authy;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author hansospina
 *         <p>
 *         Copyright © 2017 Twilio, Inc. All Rights Reserved.
 */
public class AuthyUtil {
    public static final String HEADER_AUTHY_SIGNATURE_NONCE = "X-Authy-Signature-Nonce";
    public static final String HEADER_AUTHY_SIGNATURE = "X-Authy-Signature";

    private static final Logger LOGGER = Logger.getLogger(AuthyUtil.class.getName());

    private static String hmacSha(String KEY, String VALUE) throws OneTouchException {

        try {
            SecretKeySpec signingKey = new SecretKeySpec(KEY.getBytes(UTF_8), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(VALUE.getBytes(UTF_8));
            return DatatypeConverter.printBase64Binary(rawHmac);
        } catch (Exception ex) {
            // capture the exceptions and wrap them using authy.
            throw new OneTouchException("There was an exception checking the Authy OneTouch signature.", ex);
        }
    }


    /**
     * Validates the request information to
     *
     * @param parameters The request parameters(all of them)
     * @param headers    The headers of the request
     * @param url        The url of the request.
     * @param apiKey     the security token from the authy library
     * @return true if the signature ios valid, false otherwise
     * @throws UnsupportedEncodingException if the string parameters have problems with UTF-8 encoding.
     */
    private static boolean validateSignature(Map<String, String> parameters, Map<String, String> headers, String method, String url, String apiKey) throws OneTouchException, UnsupportedEncodingException {

        if (headers == null)
            throw new OneTouchException("No headers sent");

        if (!headers.containsKey(HEADER_AUTHY_SIGNATURE))
            throw new OneTouchException("'SIGNATURE' is missing.");

        if (!headers.containsKey(HEADER_AUTHY_SIGNATURE_NONCE))
            throw new OneTouchException("'NONCE' is missing.");

        if (parameters == null || parameters.isEmpty())
            throw new OneTouchException("'PARAMS' are missing.");

        StringBuilder sb = new StringBuilder(headers.get(HEADER_AUTHY_SIGNATURE_NONCE))
                .append("|")
                .append(method)
                .append("|")
                .append(url)
                .append("|")
                .append(mapToQuery(parameters));

        String signature = hmacSha(apiKey, sb.toString());

        // let's check that the Authy signature is valid
        return signature.equals(headers.get(HEADER_AUTHY_SIGNATURE));
    }


    public static void extract(String pre, JSONObject obj, HashMap<String, String> map) {

        for (String k : obj.keySet()) {

            String key = pre.length() == 0 ? k : pre + "[" + k + "]";
            if (obj.optJSONObject(k) != null) {
                extract(key, obj.getJSONObject(k), map);

            } else if (obj.optJSONArray(k) != null) {


                JSONArray arr = obj.getJSONArray(k);

                int i = 0;

                for (Object tmp : arr) {
                    String tmpKey = key + "[" + i + "]";

                    if (tmp instanceof JSONObject) {
                        extract(tmpKey, (JSONObject) tmp, map);
                    } else {
                        map.put(tmpKey, getValue(tmp));
                    }
                    i++;
                }

            } else {
                map.put(key, getValue(obj.get(k)));
            }

        }

    }


    private static String getValue(Object val) {

        if (val instanceof Boolean) {
            return Boolean.toString(((Boolean) val));
        } else if (val instanceof Integer) {
            return Long.toString(((Integer) val));
        } else if (val instanceof Long) {
            return Long.toString(((Long) val));
        } else if (val instanceof Float) {
            return Float.toString(((Float) val));
        } else if (val instanceof Double) {
            return Double.toString(((Double) val));
        } else if (JSONObject.NULL.equals(val)) {
            return "";
        } else {
            return String.valueOf(val);
        }

    }

    public static String mapToQuery(Map<String, String> map) throws OneTouchException, UnsupportedEncodingException {

        StringBuilder sb = new StringBuilder();

        SortedSet<String> keys = new TreeSet<>(map.keySet());

        boolean first = true;

        for (String key : keys) {
            if (key.length() > 200)
                throw new OneTouchException("max number of characters of key exceeded.");

            if (first) {
                first = false;
            } else {
                sb.append("&");
            }

            String value = map.get(key);

            // don't encode null values
            if (value == null) {
                continue;
            }

            sb.append(URLEncoder.encode(key.replaceAll("\\[([0-9])*\\]", "[]"), UTF_8.name())).append("=").append(URLEncoder.encode(value, UTF_8.name()));
        }

        return sb.toString();

    }


    /**
     * Validates the request information to
     *
     * @param body    The body of the request in case of a POST method
     * @param headers The headers of the request
     * @param url     The url of the request.
     * @param apiKey  the security token from the authy library
     * @return true if the signature ios valid, false otherwise
     * @throws com.authy.OneTouchException
     * @throws UnsupportedEncodingException if the string parameters have problems with UTF-8 encoding.
     */
    public static boolean validateSignatureForPost(String body, Map<String, String> headers, String url, String apiKey) throws OneTouchException, UnsupportedEncodingException {
        HashMap<String, String> params = new HashMap<>();
        if (body == null || body.isEmpty())
            throw new OneTouchException("'PARAMS' are missing.");
        extract("", new JSONObject(body), params);
        return validateSignature(params, headers, "POST", url, apiKey);
    }

    /**
     * Validates the request information to
     *
     * @param params  The query parameters in case of a GET request
     * @param headers The headers of the request
     * @param url     The url of the request.
     * @param apiKey  the security token from the authy library
     * @return true if the signature ios valid, false otherwise
     * @throws com.authy.OneTouchException
     * @throws UnsupportedEncodingException if the string parameters have problems with UTF-8 encoding.
     */
    public static boolean validateSignatureForGet(Map<String, String> params, Map<String, String> headers, String url, String apiKey) throws OneTouchException, UnsupportedEncodingException {
        return validateSignature(params, headers, "GET", url, apiKey);
    }


    /**
     * Loads your api_key and api_url properties from the given property file
     * <p>
     * Two important things to have in mind here:
     * 1) if api_key and api_url are defined as environment variables, they will be returned as the properties.
     * 2) If you want to load your properties file have in mind your classloader path may change.
     *
     * @return the Properties object containing the properties to setup Authy or an empty Properties object if no properties were found
     */
    public static Properties loadProperties(String path, Class cls) {

        Properties properties = new Properties();

        // environment variables will always override properties file

        try {

            InputStream in = cls.getResourceAsStream(path);

            // if we cant find the properties file
            if (in != null) {
                properties.load(in);
            }


            // Env variables will always override properties
            if (System.getenv("api_key") != null && System.getenv("api_url") != null) {
                properties.put("api_key", System.getenv("api_key"));
                properties.put("api_url", System.getenv("api_url"));
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Problems loading properties", e);
        }


        return properties;
    }

}
