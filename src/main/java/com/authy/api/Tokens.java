package com.authy.api;

import com.authy.AuthyException;
import org.json.JSONObject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Julian Camargo
 */
public class Tokens extends Resource {
    public static final String TOKEN_VERIFICATION_PATH = "/protected/xml/verify/";

    public Tokens(String uri, String key) {
        super(uri, key);
    }

    public Tokens(String uri, String key, boolean testFlag) {
        super(uri, key, testFlag);
    }

    public Token verify(int userId, String token) {
        return verify(userId, token, null);
    }

    public Token verify(int userId, String token, Map<String, String> options) {
        InternalToken internalToken = new InternalToken();
        internalToken.setOption(options);

        StringBuilder path = new StringBuilder(TOKEN_VERIFICATION_PATH);
        try {
            validateToken(token);
            path.append(URLEncoder.encode(token, ENCODE)).append('/');
            path.append(URLEncoder.encode(Integer.toString(userId), ENCODE));

            String content = this.get(path.toString(), internalToken);
            return tokenFromXml(this.getStatus(), content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Token();
    }

    private Token tokenFromXml(int status, String content) {
        Token token = new Token();
        try {
            Error error = errorFromXml(status, content);

            if (error != null) {
                token.setError(error);
                return token;
            }

            JAXBContext context = JAXBContext.newInstance(Hash.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            StringReader xml = new StringReader(content);
            Hash hash = (Hash) unmarshaller.unmarshal(new StreamSource(xml));

            token = new Token(status, content, hash.getMessage());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return token;
    }

    private Error errorFromXml(int status, String content) {
        try {
            Error error;
            JAXBContext context = JAXBContext.newInstance(Error.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            StringReader xml = new StringReader(content);
            error = (Error) unmarshaller.unmarshal(new StreamSource(xml));
            return error;
        } catch (JAXBException e) {
            return null;
        }

    }

    private void validateToken(String token) throws AuthyException {
        int len = token.length();
        if (!isInteger(token)) {
            throw new AuthyException("Invalid Token. Only digits accepted.");
        }
        if (len < 6 || len > 10) {
            throw new AuthyException("Invalid Token. Unexpected length.");
        }
    }

    private boolean isInteger(String s) {
        try {
            Long.parseLong(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    class InternalToken implements Formattable {
        Map<String, String> options;

        public InternalToken() {
            options = new HashMap<>();
        }

        public void setOption(Map<String, String> options) {
            if (options != null)
                this.options = options;
        }

        public String toXML() {
            return null;
        }

        public Map<String, String> toMap() {
            if (!options.containsKey("force"))
                options.put("force", "true");

            return options;
        }

        // required to satisfy Formattable interface
        public String toJSON() {
            return new JSONObject(toMap()).toString();
        }
    }
}
