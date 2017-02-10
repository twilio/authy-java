package com.authy;

import com.authy.api.Resource;
import com.authy.api.TestSMSCode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Properties;

/**
 * Unit tests for {@link com.authy.AuthyUtil}
 *
 * @author hansospina
 *         <p>
 *         Copyright © 2016 Twilio, Inc. All Rights Reserved.
 */
public class TestAuthyUtil {

    private static Properties properties;

    static {

        InputStream in = TestSMSCode.class.getResourceAsStream("test.properties");
        properties = new Properties();

        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Before
    public void setUp() throws IOException {
        // Let's configure the API Client with the properties defined at the test.properties file.
        Assert.assertNotNull(properties.getProperty("api_key"));
        Assert.assertNotNull(properties.getProperty("api_url"));
    }


    /**
     * This test method helps the users to clearly test the signature validation with GET and POST
     *
     * @throws AuthyException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testSignature() throws AuthyException, UnsupportedEncodingException {

        Assert.assertNotNull(properties.getProperty("authy_util_signature_url"));
        Assert.assertNotNull(properties.getProperty("authy_util_signature_signature"));
        Assert.assertNotNull(properties.getProperty("authy_util_signature_nonce"));
        Assert.assertNotNull(properties.getProperty("authy_util_signature_method"));
        String url = properties.getProperty("authy_util_signature_url");

        String method = properties.getProperty("authy_util_signature_method");

        HashMap<String, String> params = new HashMap<>();

        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Authy-Signature-Nonce", properties.getProperty("authy_util_signature_nonce"));
        headers.put("X-Authy-Signature", properties.getProperty("authy_util_signature_signature"));

        // if we want to test POST, then fetch the info from the body
        if (method.equals(Resource.METHOD_POST)) {
            Assert.assertNotNull(properties.getProperty("authy_util_signature_body"));
           
            Assert.assertTrue("Invalid Signature", AuthyUtil.validateSignatureForPost(properties.getProperty("authy_util_signature_body"), headers, url, properties.getProperty("api_key")));
        } else {
            // if we want to test GET, then fetch the info from the querystring
            Assert.assertNotNull(properties.getProperty("authy_util_signature_params"));
            String tmp[] = properties.getProperty("authy_util_signature_params").split("&");

            for (String param : tmp) {
                if (param.indexOf('=') < param.length() - 1) {
                    String key = URLDecoder.decode(param.split("=")[0], "ASCII");
                    String value = URLDecoder.decode(param.split("=")[1], "ASCII");
                    params.put(key, value);
                } else {
                    String key = URLDecoder.decode(param.split("=")[0], "ASCII");
                    params.put(key, "");
                }

            }

            Assert.assertTrue("Invalid Signature", AuthyUtil.validateSignatureForGet(params, headers, url, properties.getProperty("api_key")));

        }


    }
    
    /**
     * This test method helps the users to clearly test the signature validation with GET and POST
     *
     * @throws AuthyException
     * @throws UnsupportedEncodingException
     */
    @Test (expected=AuthyException.class)
    public void testSignatureWithoutNonce() throws AuthyException, UnsupportedEncodingException {

        Assert.assertNotNull(properties.getProperty("authy_util_signature_url"));
        Assert.assertNotNull(properties.getProperty("authy_util_signature_signature"));
        Assert.assertNotNull(properties.getProperty("authy_util_signature_nonce"));
        Assert.assertNotNull(properties.getProperty("authy_util_signature_method"));
        String url = properties.getProperty("authy_util_signature_url");

        String method = properties.getProperty("authy_util_signature_method");

        HashMap<String, String> params = new HashMap<>();

        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Authy-Signature", properties.getProperty("authy_util_signature_signature"));

        // if we want to test POST, then fetch the info from the body
        if (method.equals(Resource.METHOD_POST)) {
            Assert.assertNotNull(properties.getProperty("authy_util_signature_body"));
           
            Assert.assertTrue("Invalid Signature", AuthyUtil.validateSignatureForPost(properties.getProperty("authy_util_signature_body"), headers, url, properties.getProperty("api_key")));
        } else {
            // if we want to test GET, then fetch the info from the querystring
            Assert.assertNotNull(properties.getProperty("authy_util_signature_params"));
            String tmp[] = properties.getProperty("authy_util_signature_params").split("&");

            for (String param : tmp) {
                if (param.indexOf('=') < param.length() - 1) {
                    String key = URLDecoder.decode(param.split("=")[0], "ASCII");
                    String value = URLDecoder.decode(param.split("=")[1], "ASCII");
                    params.put(key, value);
                } else {
                    String key = URLDecoder.decode(param.split("=")[0], "ASCII");
                    params.put(key, "");
                }

            }

            Assert.assertTrue("Invalid Signature", AuthyUtil.validateSignatureForGet(params, headers, url, properties.getProperty("api_key")));

        }
    }



    @Test (expected=AuthyException.class)
    public void testSignatureWithoutParams() throws AuthyException, UnsupportedEncodingException {

        Assert.assertNotNull(properties.getProperty("authy_util_signature_url"));
        Assert.assertNotNull(properties.getProperty("authy_util_signature_signature"));
        Assert.assertNotNull(properties.getProperty("authy_util_signature_nonce"));
        Assert.assertNotNull(properties.getProperty("authy_util_signature_method"));
        String url = properties.getProperty("authy_util_signature_url");

        String method = properties.getProperty("authy_util_signature_method");

        HashMap<String, String> params = new HashMap<>();

        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Authy-Signature-Nonce", properties.getProperty("authy_util_signature_nonce"));
        headers.put("X-Authy-Signature", properties.getProperty("authy_util_signature_signature"));

        // if we want to test POST, then fetch the info from the body
        if (method.equals(Resource.METHOD_POST)) {
            Assert.assertNotNull(properties.getProperty("authy_util_signature_body"));
           
            Assert.assertTrue("Invalid Signature", AuthyUtil.validateSignatureForPost(null, headers, url, properties.getProperty("api_key")));
        } else {
            // if we want to test GET, then fetch the info from the querystring
            Assert.assertNotNull(properties.getProperty("authy_util_signature_params"));
            String tmp[] = properties.getProperty("authy_util_signature_params").split("&");


            Assert.assertTrue("Invalid Signature", AuthyUtil.validateSignatureForGet(null, headers, url, properties.getProperty("api_key")));

        }
    }

}
