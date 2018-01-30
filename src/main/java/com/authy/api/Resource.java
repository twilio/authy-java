package com.authy.api;

import com.authy.AuthyApiClient;
import com.authy.AuthyException;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;

/**
 * Class to send http requests.
 *
 * @author Julian Camargo
 */
public class Resource {
    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_PUT = "PUT";
    public static final String ENCODE = "UTF-8";
    public static final String XML_CONTENT_TYPE = "application/xml";
    public static final String JSON_CONTENT_TYPE = "application/json";
    private static final Logger LOGGER = Logger.getLogger(Resource.class.getName());
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
        setContentType(contentType);
    }

    public Resource(String uri, String key, boolean testFlag) {
        apiUri = uri;
        apiKey = key;
        this.testFlag = testFlag;
    }

    public Resource(String uri, String key, boolean testFlag, String contentType) {
        apiUri = uri;
        apiKey = key;
        this.testFlag = testFlag;
        setContentType(contentType);
    }

    /**
     * POST method.
     *
     * @param path
     * @param data
     * @return response from API.
     */
    public String post(String path, Formattable data) throws AuthyException {
        return request(Resource.METHOD_POST, path, data, getDefaultOptions());
    }

    /**
     * GET method.
     *
     * @param path
     * @param data
     * @return response from API.
     */
    public String get(String path, Formattable data) throws AuthyException {
        return request(Resource.METHOD_GET, path, data, getDefaultOptions());
    }

    /**
     * PUT method.
     *
     * @param path
     * @param data
     * @return response from API.
     */
    public String put(String path, Formattable data) throws AuthyException {
        return request(Resource.METHOD_PUT, path, data, getDefaultOptions());
    }

    /**
     * DELETE method.
     *
     * @param path
     * @param data
     * @return response from API.
     */
    public String delete(String path, Formattable data) throws AuthyException {
        return request("DELETE", path, data, getDefaultOptions());
    }

    public String request(String method, String path, Formattable data, Map<String, String> options) throws AuthyException {
        HttpURLConnection connection;
        String answer = null;

        try {
            StringBuilder sb = new StringBuilder();

            if (method.equals(Resource.METHOD_GET)) {
                sb.append(prepareGet(data));
            }

            URL url = new URL(apiUri + path + sb.toString());
            connection = createConnection(url, method, options);

            connection.setRequestProperty("X-Authy-API-Key", apiKey);

            // data might be sent as a null value for cases like "DELETE" requests
            if (data!= null && data.toMap().containsKey("api_key")) {
                LOGGER.log(Level.WARNING, "Found 'api_key' as a parameter, please remove it, Authy-Java already handles the'api_key' for you.");
            }
            if (method.equals(Resource.METHOD_POST) || method.equals(Resource.METHOD_PUT)) {
                if (isJSON) {
                    writeJson(connection, data);
                } else {
                    writeXml(connection, data);
                }
            }

            status = connection.getResponseCode();
            answer = getResponse(connection);
        } catch (SSLHandshakeException e) {
            System.err.println("SSL verification is failing. This might be because of an attack. Contact support@authy.com");
        } catch (MalformedURLException e) {
            throw new AuthyException("Invalid host", e);
        } catch (IOException e) {
            throw new AuthyException("Connection error", e);
        }
        return answer;
    }

    /**
     * Get status from response.
     *
     * @return status code.
     */
    public int getStatus() {
        return status;
    }

    public String getContentType() {
        return this.contentType;
    }

    private void setContentType(String contentType) {
        this.contentType = (contentType == null || contentType.equals(XML_CONTENT_TYPE) || !contentType.equals(JSON_CONTENT_TYPE)) ? XML_CONTENT_TYPE : JSON_CONTENT_TYPE;
        isJSON = this.contentType.equals(JSON_CONTENT_TYPE);
    }

    protected HttpURLConnection createConnection(URL url, String method,
                                                 Map<String, String> options) throws IOException {


        HttpURLConnection connection;
        if (testFlag)
            connection = (HttpURLConnection) url.openConnection();
        else
            connection = (HttpsURLConnection) url.openConnection();

        connection.setRequestMethod(method);

        for (Entry<String, String> s : options.entrySet()) {
            connection.setRequestProperty(s.getKey(), s.getValue());
        }

        connection.setDoOutput(true);

        return connection;
    }

    private String getResponse(HttpURLConnection connection) throws IOException {
        InputStream in;
        // Load stream
        if (status != 200) {
            in = connection.getErrorStream();
        } else {
            in = connection.getInputStream();
        }

        BufferedInputStream input = new BufferedInputStream(in);
        StringBuilder sb = new StringBuilder();
        int ch;

        while ((ch = input.read()) != -1) {
            sb.append((char) ch);
        }
        input.close();

        return sb.toString();
    }

    private void writeXml(HttpURLConnection connection, Formattable data) throws SSLHandshakeException, IOException {
        if (data == null)
            return;

        OutputStream os = connection.getOutputStream();

        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(os));
        output.write(data.toXML());
        output.flush();
        output.close();
    }

    private void writeJson(HttpURLConnection connection, Formattable data) throws SSLHandshakeException, IOException {
        if (data == null)
            return;

        OutputStream os = connection.getOutputStream();
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(os));
        output.write(data.toJSON());
        output.flush();
        output.close();
    }


    private String prepareGet(Formattable data) {

        if (data == null)
            return "";

        StringBuilder sb = new StringBuilder("?");
        Map<String, String> params = data.toMap();

        boolean first = true;

        for (Entry<String, String> s : params.entrySet()) {

            if (first) {
                first = false;
            } else {
                sb.append('&');
            }

            try {
                sb.append(URLEncoder.encode(s.getKey(), ENCODE)).append("=").append(URLEncoder.encode(s.getValue(), ENCODE));
            } catch (UnsupportedEncodingException e) {
                System.out.println("Encoding not supported" + e.getMessage());
            }
        }


        return sb.toString();
    }

    private Map<String, String> getDefaultOptions() {
        if (this.defaultOptions == null || this.defaultOptions.isEmpty()) {
            this.defaultOptions = new HashMap<>();
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
