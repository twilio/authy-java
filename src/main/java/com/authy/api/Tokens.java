package com.authy.api;

import com.authy.AuthyException;
import org.json.JSONException;
import org.json.JSONObject;
import sun.net.www.protocol.http.HttpURLConnection;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Julian Camargo
 */
public class Tokens extends Resource {
    public static final String TOKEN_VERIFICATION_PATH = "/protected/json/verify/";

    public Tokens(String uri, String key) {
        super(uri, key);
    }

    public Tokens(String uri, String key, boolean testFlag) {
        super(uri, key, testFlag);
    }

    public Token verify(int userId, String token) throws AuthyException {
        return verify(userId, token, null);
    }

    public Token verify(int userId, String token, Map<String, String> options) throws AuthyException {
        InternalToken internalToken = new InternalToken();
        internalToken.setOption(options);

        StringBuilder path = new StringBuilder(TOKEN_VERIFICATION_PATH);
        validateToken(token);
        path.append(token).append('/');
        path.append(userId);

        final Response response = this.get(path.toString(), internalToken);
        return tokenFromJson(response.getStatus(), response.getBody());
    }

    private Token tokenFromJson(int status, String content) throws AuthyException {
        if (status == 200) {
            try {
                JSONObject tokenJSON = new JSONObject(content);
                String message = tokenJSON.optString("message");
                return new Token(status, content, message);

            } catch (JSONException e) {
                throw new AuthyException("Invalid response from server", e, status);
            }
        }

        final Error error = errorFromJson(content);
        throw new AuthyException("Invalid token", status, error.getCode());
    }

    private void validateToken(String token) throws AuthyException {
        int len = token.length();
        if (!isInteger(token)) {
            throw new AuthyException("Invalid Token. Only digits accepted.", HttpURLConnection.HTTP_UNAUTHORIZED,
                    Error.Code.TOKEN_INVALID);
        }
        if (len < 6 || len > 10) {
            throw new AuthyException("Invalid Token. Unexpected length.", HttpURLConnection.HTTP_UNAUTHORIZED,
                    Error.Code.TOKEN_INVALID);
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

        InternalToken() {
            options = new HashMap<>();
        }

        void setOption(Map<String, String> options) {
            if (options != null) {
                this.options = options;
            }
        }

        public String toXML() {
            return null;
        }

        public Map<String, String> toMap() {
            if (!options.containsKey("force")) {
                options.put("force", "true");
            }

            return options;
        }
    }
}
