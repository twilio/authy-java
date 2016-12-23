package com.authy;

import com.authy.api.Resource;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author hansospina
 *         <p>
 *         Copyright © 2016 Twilio, Inc. All Rights Reserved.
 */
public class AuthyUtil {

    private static final Logger LOGGER = Logger.getLogger(Resource.class.getName());

    private static String hmacSha(String KEY, String VALUE) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(KEY.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(VALUE.getBytes("UTF-8"));
            return DatatypeConverter.printBase64Binary(rawHmac);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Validates the request information to
     *
     * @param parameters The request parameters(all of them)
     * @param headers    The headers of the request
     * @param method     The method, should be GET or POST
     * @param url        The url of the request.
     * @param authyToken the security token from the authy library
     * @return true if the signature ios valid, false otherwise
     * @throws UnsupportedEncodingException if the string parameters have problems with UTF-8 encoding.
     */
    public static boolean validateSignature(Map<String, String> parameters, Map<String, String> headers, String method, String url, String authyToken) throws UnsupportedEncodingException {

        // check
        if (method == null || (!method.equalsIgnoreCase(Resource.METHOD_POST) && !method.equalsIgnoreCase(Resource.METHOD_GET))) {
            return false;
        }

        SortedSet<String> keys = new TreeSet<>(parameters.keySet());


        StringBuilder sb = new StringBuilder(headers.get("X-Authy-Signature-Nonce"))
                .append("|")
                .append(method)
                .append("|")
                .append(url)
                .append("|");

        boolean first = true;

        for (String key : keys) {

            if (first) {
                first = false;
            } else {
                sb.append("&");
            }

            String value = parameters.get(key);

            // don't encode null values
            if (value == null) {
                continue;
            }

            sb.append(URLEncoder.encode(key, "UTF-8")).append("=").append(URLEncoder.encode(value, "UTF-8"));
        }

        String signature = hmacSha(authyToken, sb.toString());

        // let's check that the Authy signature is valid
        return signature.equals(headers.get("X-Authy-Signature"));
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
