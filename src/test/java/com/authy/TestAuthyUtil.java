package com.authy;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for {@link com.authy.AuthyUtil}
 *
 * @author hansospina
 *         <p>
 *         Copyright © 2016 Twilio, Inc. All Rights Reserved.
 */
public class TestAuthyUtil {
    private final String testApikey = "FU10H0uCafgKnXvPfDm5iAisVuHDgXJA";
    private final String testCallbackUrl = "https://requestb.in/ui9vdiui";


    @Test
    public void testValidSignatureApprovedPost() throws UnsupportedEncodingException, OneTouchException {
        final String testNonce = "1512923419";
        final String expectedSignature = "6uKAaKxCkkFEyQujwdudyPNUgRi2fY5otoCOQN7VjCw=";
        final String callbackBody = "{\"authy_id\":688611,\"device_uuid\":2102943,\"callback_action\":\"approval_request_status\",\"uuid\":\"4a725520-bff5-0135-eb01-0e5d90336a8c\",\"status\":\"approved\",\"approval_request\":{\"transaction\":{\"details\":null,\"device_details\":{},\"device_geolocation\":\"\",\"device_signing_time\":1512923419,\"encrypted\":false,\"flagged\":false,\"hidden_details\":null,\"message\":\"Authorize OneTouch Unit Test\",\"reason\":\"\",\"requester_details\":null,\"status\":\"approved\",\"uuid\":\"4a725520-bff5-0135-eb01-0e5d90336a8c\",\"created_at_time\":1512923400,\"customer_uuid\":15083},\"expiration_timestamp\":1513009800,\"logos\":null,\"app_id\":\"582380eb7e1caa03317ec08c\"},\"signature\":\"pQmypvnmoIb7qIHrKcd3nwPArh8Ecr8L87XTYqqAfagDkXhOD7CulSdDkE0ImFBzigwc+vxwZsgxBnhFmU2c9kYuvMJiPLeS8NCpRucg7eHeGPM0jQbKveqzFcZ9L6P1kRHjSYwS7dLqEINBffNckK7O9LHz13XYklxXvYUwvemtj+yEemyCJbmlJbCSlUTyajr3WSRPMYZV7xXTNtWp2XvcRSclP8izgA1cV/cw7ctDYIPG6wUXJGSIs/kg3hTDeN1Z3YBq1fnMkfxeb5g9bRveRlCjXpQ6xFh1wQEUbbtJpf+uRgxddbQwxfda9gIb5osOEhKRv5HoJ03yOvKiBQ==\",\"device\":{\"city\":null,\"country\":\"Colombia\",\"ip\":\"190.130.65.251\",\"region\":null,\"registration_city\":null,\"registration_country\":\"Colombia\",\"registration_ip\":\"190.130.66.242\",\"registration_method\":\"sms\",\"registration_region\":null,\"os_type\":\"android\",\"last_account_recovery_at\":null,\"id\":2102943,\"registration_date\":1505418165,\"last_sync_date\":1505418173}}";

        Map<String, String> headers = createHeaders(testNonce, expectedSignature);
        final boolean valid = AuthyUtil.validateSignatureForPost(callbackBody, headers, testCallbackUrl, testApikey);

        assertThat(valid, is(true));
    }

    @Test
    public void testValidSignatureDeniedWithDetailsPost() throws UnsupportedEncodingException, OneTouchException {
        final String testNonce = "1512940231";
        final String expectedSignature = "HFc5mICOVRrEmpmBoIuubKWcN0YYd50TaO7YQJFrnlM=";
        final String callbackBody = "{\"authy_id\":688611,\"device_uuid\":2102943,\"callback_action\":\"approval_request_status\",\"uuid\":\"6edb8f40-c01c-0135-c648-0e00ace7f69c\",\"status\":\"denied\",\"approval_request\":{\"transaction\":{\"details\":{\"username\":\"User\",\"location\":\"California,USA\",\"cosa1\":\"cosa1\",\"cosa2\":\"cosa2\"},\"device_details\":{},\"device_geolocation\":\"\",\"device_signing_time\":1512940232,\"encrypted\":false,\"flagged\":false,\"hidden_details\":{\"ip_address\":\"10.10.3.203\"},\"message\":\"Authorize OneTouch Unit Test\",\"reason\":\"\",\"requester_details\":null,\"status\":\"denied\",\"uuid\":\"6edb8f40-c01c-0135-c648-0e00ace7f69c\",\"created_at_time\":1512940211,\"customer_uuid\":15083},\"expiration_timestamp\":1513026611,\"logos\":null,\"app_id\":\"582380eb7e1caa03317ec08c\"},\"signature\":\"jTLeK9tHUAhJRexjBIq3DVowPRKE+8578YzJD5yizXqYqmeQT7t8NS1uFfbmjHKabgIvC0N/WEFfyvKWjARrNVR6FJ5EjbOZJy7ouQT+9iTaorsJDVPUPeVnQUUTi3noXcSonGN0+YW7foHf8zMnTTyQBbjurexv2dkfu0fLdiF8I6xRhMeq5sf5APdZCt7NsFIM95N0wO6MHoD5sLL8yFrB1C/RB35n6BIxgTWz0TjtbcO+V/rqjgMK47xTITPsbEo46ammhRl4vU5flcM2O6KE6Q7tKLCftAzs/3xu13w1KKkFXmCqXpeB29lSNU2wveGI7nB2eIk41medDJ81Dg==\",\"device\":{\"city\":null,\"country\":\"Colombia\",\"ip\":\"190.130.65.251\",\"region\":null,\"registration_city\":null,\"registration_country\":\"Colombia\",\"registration_ip\":\"190.130.66.242\",\"registration_method\":\"sms\",\"registration_region\":null,\"os_type\":\"android\",\"last_account_recovery_at\":null,\"id\":2102943,\"registration_date\":1505418165,\"last_sync_date\":1505418173}}";

        Map<String, String> headers = createHeaders(testNonce, expectedSignature);
        final boolean valid = AuthyUtil.validateSignatureForPost(callbackBody, headers, testCallbackUrl, testApikey);

        assertThat(valid, is(true));
    }

    @Test
    public void testValidSignatureApprovedWithDetailsAndLogosPost() throws UnsupportedEncodingException, OneTouchException {
        final String testNonce = "1512940917";
        final String expectedSignature = "IFD2P2f5w2Jis1uref9Blu7a0liztOsnso16Gh6y054=";
        final String callbackBody = "{\"authy_id\":688611,\"device_uuid\":2102943,\"callback_action\":\"approval_request_status\",\"uuid\":\"098c3580-c01e-0135-eb00-0e5d90336a8c\",\"status\":\"approved\",\"approval_request\":{\"transaction\":{\"details\":{\"username\":\"User\",\"location\":\"California,USA\",\"cosa1\":\"cosa1\",\"cosa2\":\"cosa2\"},\"device_details\":{},\"device_geolocation\":\"\",\"device_signing_time\":1512940918,\"encrypted\":false,\"flagged\":false,\"hidden_details\":{\"ip_address\":\"10.10.3.203\"},\"message\":\"Authorize OneTouch Unit Test\",\"reason\":\"\",\"requester_details\":null,\"status\":\"approved\",\"uuid\":\"098c3580-c01e-0135-eb00-0e5d90336a8c\",\"created_at_time\":1512940900,\"customer_uuid\":15083},\"expiration_timestamp\":1513027300,\"logos\":[{\"res\":\"default\",\"url\":\"https://www.itsalllost.com/wp-content/uploads/2017/04/twilio-logo-red.png\"},{\"res\":\"med\",\"url\":\"https://www.itsalllost.com/wp-content/uploads/2017/04/twilio-logo-red.png\"}],\"app_id\":\"582380eb7e1caa03317ec08c\"},\"signature\":\"qOLMuHVy4KITm2nSd1PxJCv+ydjcduKwxz2Fc7pMrDm7QtU2hMAnY5AUxdwlae5WJmEWNM8OctdGhMJweTwICkkOgYm2v+u7k/wz5zuPozDDnMqJWBjiCfbKNpKqf8CQ2dBndtxi2Sl7/y57KiXYJfTlGHNBhCoTxVzVBNDEPu6OLV6KA60mcEW87tg1b4Q/p69ZkYb5B1f9Ujk/ueCbCK6JDhtUf1v3/baNgO8o/mp2EydiFughqpiHIIOR0VY9/o/hh5a7z6FG4OxZ3WmS7q2506Wy698LW3ZRNl0aXwtFIat4IyCSSuDQFW9LZEcXdQK4YixSJ8b5H8vlRErSPQ==\",\"device\":{\"city\":null,\"country\":\"Colombia\",\"ip\":\"190.130.65.251\",\"region\":null,\"registration_city\":null,\"registration_country\":\"Colombia\",\"registration_ip\":\"190.130.66.242\",\"registration_method\":\"sms\",\"registration_region\":null,\"os_type\":\"android\",\"last_account_recovery_at\":null,\"id\":2102943,\"registration_date\":1505418165,\"last_sync_date\":1505418173}}";

        Map<String, String> headers = createHeaders(testNonce, expectedSignature);
        final boolean valid = AuthyUtil.validateSignatureForPost(callbackBody, headers, testCallbackUrl, testApikey);

        assertThat(valid, is(true));
    }

    @Test
    public void testValidSignatureApprovedGet() throws UnsupportedEncodingException, OneTouchException {
        final String testNonce = "1512937258";
        final String expectedSignature = "W0YumwSPoL+2CX1q4XoUSY5gOmWmnrkXUmlm7NE6iGY=";
        final String callbackQueryString = "approval_request%5Bapp_id%5D=582380eb7e1caa03317ec08c&approval_request%5Bexpiration_timestamp%5D=1513023630&approval_request%5Blogos%5D=&approval_request%5Btransaction%5D%5Bcreated_at_time%5D=1512937230&approval_request%5Btransaction%5D%5Bcustomer_uuid%5D=15083&approval_request%5Btransaction%5D%5Bdetails%5D=&approval_request%5Btransaction%5D%5Bdevice_geolocation%5D=&approval_request%5Btransaction%5D%5Bdevice_signing_time%5D=1512937257&approval_request%5Btransaction%5D%5Bencrypted%5D=false&approval_request%5Btransaction%5D%5Bflagged%5D=false&approval_request%5Btransaction%5D%5Bhidden_details%5D=&approval_request%5Btransaction%5D%5Bmessage%5D=Authorize+OneTouch+Unit+Test&approval_request%5Btransaction%5D%5Breason%5D=&approval_request%5Btransaction%5D%5Brequester_details%5D=&approval_request%5Btransaction%5D%5Bstatus%5D=approved&approval_request%5Btransaction%5D%5Buuid%5D=7e272da0-c015-0135-eafc-0e5d90336a8c&authy_id=688611&callback_action=approval_request_status&device%5Bcity%5D=&device%5Bcountry%5D=Colombia&device%5Bid%5D=2102943&device%5Bip%5D=190.130.65.251&device%5Blast_account_recovery_at%5D=&device%5Blast_sync_date%5D=1505418173&device%5Bos_type%5D=android&device%5Bregion%5D=&device%5Bregistration_city%5D=&device%5Bregistration_country%5D=Colombia&device%5Bregistration_date%5D=1505418165&device%5Bregistration_ip%5D=190.130.66.242&device%5Bregistration_method%5D=sms&device%5Bregistration_region%5D=&device_uuid=2102943&signature=YnZe2qSWEAYAROAhykwbeIOV2Ym%2Fg4y9rlIQc6ePtvTt9UDDotl7p2H%2FpC3EFNG5XsDaMkJuZmXSd0UW%2FtiuR2l%2BJ%2Fvta5yXVArq6d1uNspB1u%2BWYjumDhTLFQI0Ox6BGQMhTlWkQK96dh0bJQqyPP7I5f4xQZMmNIClCKZa%2BzUqmPA7zo1Qokz9w0u917zKt%2BsLLxLLXblhdYvFIvfVqGAtiBQzbUh9UCCuOp6jcF7HkZiGs2nFIAlwVFffhK%2BxXnEXvG9yA3KnMRX6Y31yxWd3ApRoDNbLpCOSgFlkYAasQ8hBkcSy3AyyfT4TMhyeemI3IW6GFTADrXXO%2BzvpcA%3D%3D&status=approved&uuid=7e272da0-c015-0135-eafc-0e5d90336a8c";

        Map<String, String> headers = createHeaders(testNonce, expectedSignature);
        Map<String, String> params = extractQueryParams(callbackQueryString);
        final boolean valid = AuthyUtil.validateSignatureForGet(params, headers, testCallbackUrl, testApikey);

        assertThat(valid, is(true));
    }

    @Test
    public void testValidSignatureApprovedWithDetailsGet() throws UnsupportedEncodingException, OneTouchException {
        final String testNonce = "1512942246";
        final String expectedSignature = "oCSbVsRS7uwKH1bRAnyem3pz9rTBSMpcEaS6W33DqmM=";
        final String callbackQueryString = "approval_request%5Bapp_id%5D=582380eb7e1caa03317ec08c&approval_request%5Bexpiration_timestamp%5D=1513028630&approval_request%5Blogos%5D=&approval_request%5Btransaction%5D%5Bcreated_at_time%5D=1512942230&approval_request%5Btransaction%5D%5Bcustomer_uuid%5D=15083&approval_request%5Btransaction%5D%5Bdetails%5D%5Bcosa1%5D=cosa1&approval_request%5Btransaction%5D%5Bdetails%5D%5Bcosa2%5D=cosa2&approval_request%5Btransaction%5D%5Bdetails%5D%5Blocation%5D=California%2CUSA&approval_request%5Btransaction%5D%5Bdetails%5D%5Busername%5D=User&approval_request%5Btransaction%5D%5Bdevice_geolocation%5D=&approval_request%5Btransaction%5D%5Bdevice_signing_time%5D=1512942247&approval_request%5Btransaction%5D%5Bencrypted%5D=false&approval_request%5Btransaction%5D%5Bflagged%5D=false&approval_request%5Btransaction%5D%5Bhidden_details%5D%5Bip_address%5D=10.10.3.203&approval_request%5Btransaction%5D%5Bmessage%5D=Authorize+OneTouch+Unit+Test&approval_request%5Btransaction%5D%5Breason%5D=&approval_request%5Btransaction%5D%5Brequester_details%5D=&approval_request%5Btransaction%5D%5Bstatus%5D=approved&approval_request%5Btransaction%5D%5Buuid%5D=224fdf40-c021-0135-fa62-06ca50569adc&authy_id=688611&callback_action=approval_request_status&device%5Bcity%5D=&device%5Bcountry%5D=Colombia&device%5Bid%5D=2102943&device%5Bip%5D=190.130.65.251&device%5Blast_account_recovery_at%5D=&device%5Blast_sync_date%5D=1505418173&device%5Bos_type%5D=android&device%5Bregion%5D=&device%5Bregistration_city%5D=&device%5Bregistration_country%5D=Colombia&device%5Bregistration_date%5D=1505418165&device%5Bregistration_ip%5D=190.130.66.242&device%5Bregistration_method%5D=sms&device%5Bregistration_region%5D=&device_uuid=2102943&signature=rRf5hjOsPql%2Fumb%2FzI6azWUx2Xx8s6hhKeOYXob0tqhIA7WUtUcvXDNfn%2BX%2FnAqOiBDcGr41aNU6mFw1nCbQI3jwtm9n%2F7RVtxrJN%2B85370dY0nOUpl19IGJ6xwyRa0U76svfePBROnrobGdyCvtHw6G4tT%2BJ3oo2T7Ji1TZ4scFLaRfiA95VmFYgJd6tEqoBom%2FtX8itKyxa%2FFTVSc8OriSusyX8GpxqSAKl9GjVKGp7W0p%2FGTzTU9lzVAIPHsIyX9%2FH%2BKUGk5d7oglcPb%2B0HX2V1ruSIg5IId5j3yrBPd%2Bjxf3HLTcrPMGjFZJhapMOwE4fgEWG9p6pfBYOyyauw%3D%3D&status=approved&uuid=224fdf40-c021-0135-fa62-06ca50569adc";

        Map<String, String> headers = createHeaders(testNonce, expectedSignature);
        Map<String, String> params = extractQueryParams(callbackQueryString);
        final boolean valid = AuthyUtil.validateSignatureForGet(params, headers, testCallbackUrl, testApikey);

        assertThat(valid, is(true));
    }

    @Test
    public void testValidSignatureApprovedWithDetailsAndLogosGet() throws UnsupportedEncodingException, OneTouchException {
        final String testNonce = "1512943081";
        final String expectedSignature = "5TM5uf+8WlPMjkREHeS39XUHv8CHjhq3/u/+/lWU7cg=";
        final String callbackQueryString = "approval_request%5Bapp_id%5D=582380eb7e1caa03317ec08c&approval_request%5Bexpiration_timestamp%5D=1513029464&approval_request%5Blogos%5D%5B%5D%5Bres%5D=default&approval_request%5Blogos%5D%5B%5D%5Burl%5D=https%3A%2F%2Fwww.itsalllost.com%2Fwp-content%2Fuploads%2F2017%2F04%2Ftwilio-logo-red.png&approval_request%5Btransaction%5D%5Bcreated_at_time%5D=1512943064&approval_request%5Btransaction%5D%5Bcustomer_uuid%5D=15083&approval_request%5Btransaction%5D%5Bdetails%5D%5Bcosa1%5D=cosa1&approval_request%5Btransaction%5D%5Bdetails%5D%5Bcosa2%5D=cosa2&approval_request%5Btransaction%5D%5Bdetails%5D%5Blocation%5D=California%2CUSA&approval_request%5Btransaction%5D%5Bdetails%5D%5Busername%5D=User&approval_request%5Btransaction%5D%5Bdevice_geolocation%5D=&approval_request%5Btransaction%5D%5Bdevice_signing_time%5D=1512943081&approval_request%5Btransaction%5D%5Bencrypted%5D=false&approval_request%5Btransaction%5D%5Bflagged%5D=false&approval_request%5Btransaction%5D%5Bhidden_details%5D%5Bip_address%5D=10.10.3.203&approval_request%5Btransaction%5D%5Bmessage%5D=Authorize+OneTouch+Unit+Test&approval_request%5Btransaction%5D%5Breason%5D=&approval_request%5Btransaction%5D%5Brequester_details%5D=&approval_request%5Btransaction%5D%5Bstatus%5D=approved&approval_request%5Btransaction%5D%5Buuid%5D=133c7590-c023-0135-fa61-06ca50569adc&authy_id=688611&callback_action=approval_request_status&device%5Bcity%5D=&device%5Bcountry%5D=Colombia&device%5Bid%5D=2102943&device%5Bip%5D=190.130.65.251&device%5Blast_account_recovery_at%5D=&device%5Blast_sync_date%5D=1505418173&device%5Bos_type%5D=android&device%5Bregion%5D=&device%5Bregistration_city%5D=&device%5Bregistration_country%5D=Colombia&device%5Bregistration_date%5D=1505418165&device%5Bregistration_ip%5D=190.130.66.242&device%5Bregistration_method%5D=sms&device%5Bregistration_region%5D=&device_uuid=2102943&signature=Q%2FTaHEbdfmBBw%2BT%2BgVBrM8Sw%2BdqT%2FnhdO6BJNc%2F7herUg4BxAwziQhdmQhqjY2nvtVJkWEa9PdB11tNjTcbMQs8cchyPPNLUvp8L7C82snxcH%2Fgdde65Z%2B39Aug5hCcXUoQ92PsRckvexkrCQASDWbvmJZnjVM5t3j%2BKXn2QVyZkc63GBE1W9GPNM7bmlL2ZOENPoxpUq9%2B%2FawTEb8WMDebDcFEPGSgNFcvk6%2FJTSwjzmmm8Ypk82s4P1lUq74WWxaDM7XJH2Q4mo9vHkDaRIweEx%2BliMqUQYoCtApD0vsPSe4Exfp5tfcelWE9xrte%2FF3qKiPSnV8xS0SY9L20%2F7A%3D%3D&status=approved&uuid=133c7590-c023-0135-fa61-06ca50569adc";

        Map<String, String> headers = createHeaders(testNonce, expectedSignature);
        Map<String, String> params = extractQueryParams(callbackQueryString);
        final boolean valid = AuthyUtil.validateSignatureForGet(params, headers, testCallbackUrl, testApikey);

        assertThat(valid, is(true));
    }

    private Map<String, String> extractQueryParams(String callbackQueryString) {
        return Arrays.stream(callbackQueryString.split("&")).map(it -> {
            final int idx = it.indexOf("=");
            final String key = URLDecoder.decode(idx > 0 ? it.substring(0, idx) : it);
            final String value = URLDecoder.decode(idx > 0 && it.length() > idx + 1 ? it.substring(idx + 1) : "");
            return new SimpleImmutableEntry<>(key, value);
        }).collect(toMap(SimpleImmutableEntry::getKey, SimpleImmutableEntry::getValue));
    }

    private Map<String, String> createHeaders(String nonce, String signature) {
        Map<String, String> headers = new HashMap<>();
        headers.put(AuthyUtil.HEADER_AUTHY_SIGNATURE_NONCE, nonce);
        headers.put(AuthyUtil.HEADER_AUTHY_SIGNATURE, signature);
        return headers;
    }
}
