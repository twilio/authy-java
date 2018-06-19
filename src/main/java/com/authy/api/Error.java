package com.authy.api;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement(name = "errors")
public class Error implements Formattable {

    public enum Code {
        DEFAULT_ERROR(6000),
        API_KEY_INVALID(60001),
        INVALID_REQUEST(60002),
        INVALID_PARAMETER(60004),
        INVALID_ENCODING(60005),
        TOKEN_VIA_CALL_ADDON_DISABLED(60006),
        SMS_DISABLED(60007),
        ACCOUNT_SUSPENDED(60008),
        MONTHLY_SMS_LIMIT_REACHED(60009),
        DAILY_SMS_LIMIT_REACHED(60010),
        MONTHLY_CALLS_LIMIT_REACHED(60011),
        DAILY_CALLS_LIMIT_REACHED(60012),
        BANNED_COUNTRY(60013),
        CALL_NOT_STARTED(60014),
        SMS_NOT_SENT(60015),
        USER_DOES_NOT_EXIST(60016),
        USER_SUSPENDED(60017),
        USER_DISABLED(60018),
        REUSED_TOKEN(60019),
        TOKEN_INVALID(60020),
        CANNOT_CREATE_PHONE_VERIFICATION(60021),
        PHONE_VERIFICATION_INCORRECT(60022),
        PHONE_VERIFICATION_NOT_FOUND(60023),
        CANNOT_GET_PHONE_INFO(60024),
        PHONE_INFO_ERROR_QUERYING(60025),
        USER_NOT_FOUND(60026),
        USER_NOT_VALID(60027),
        COULD_NOT_DELETE_USER(60028),
        CANNOT_CREATE_ACTIVITY(60029),
        USER_INCORRECT_PARAMS(60030),
        ACTION_NOT_AUTHORIZED(60031),
        SMS_NOT_FOUND(60032),
        INVALID_PHONE_NUMBER(60033),
        REGISTRATION_REQUEST_INVALID(60034),
        REGISTRATION_REQUEST_NOT_FOUND(60035),
        REGISTRATION_INVALID_PIN(60036),
        REGISTRATION_EXPIRED(60037),
        INVALID_EMAIL(60038),
        PHONE_VERIFICATION_PARAMS_INVALID(60042),
        TWILIO_API_KEY_DETECTED(60047),
        ONETOUCH_APPROVAL_REQUEST_NOT_FOUND(60049),
        ONETOUCH_UNREGISTERED_USER(60050),
        ONETOUCH_DEVICE_NOT_FOUND(60051),
        ONETOUCH_INTERNAL_CONNECTION_ERROR(60052),
        ONETOUCH_SENDING_APPROVAL_REQUEST_ERROR(60053),
        ONETOUCH_APPROVAL_REQUEST_ERROR(60054),
        ONETOUCH_NOTIFYING_CUSTOMER_ERROR(60055),
        MUST_USE_SSL(60056),
        ACCOUNT_SUSPENDED_TEMPORARILY(60057),
        PHONE_NUMBER_NOT_FOUND(60058),
        PHONE_NUMBER_INVALID(60059),
        TWILIO_ACCOUNT_SUSPENDED(60060),
        APPLICATION_SUSPENDED(60061),
        DISALLOWED_IP(60063),
        CANNOT_ENABLE_ONETOUCH(60064),
        ONETOUCH_CANNOT_SAVE_CALLBACK(60066),
        CANNOT_UPDATE_ON_DEVICE_REGISTRATION(60068),
        ACCESS_KEY_ERROR(60069),
        INVALID_APPLICATION(60070),
        ACCESS_KEY_NOT_FOUND(60071),
        INVALID_ACCESS_KEY(60072),
        INVALID_APPLICATION_API_KEY(60073),
        ACCESS_KEY_PERMISSION_DENIED(60074),
        CANNOT_DELETE_APPLICATION(60075),
        COUNTRY_CODE_VALIDATION_FAIL(60078),
        ONETOUCH_APPROVAL_REQUEST_NOT_PENDING(60079),
        ONETOUCH_APPROVAL_REQUEST_INVALID(60080),
        CANNOT_SEND_SMS_TO_LANDLINE(60082),
        PHONE_NUMBER_NOT_PROVISIONED(60083),
        JWT_TOKEN_EXPIRED(60086),
        INVALID_SIGNATURE(60087),
        INVALID_REPORTING_QUERY(60089),
        REGISTRATION_REQUEST_COULD_NOT_BE_CREATED(60090),
        CUSTOM_MESSAGE_DISALLOWED(60091),
        DEVICE_NOT_FOUND(60092),
        SDK_DEVICE_NOT_DELETED(60093),
        INVALID_REPORTING_INTERVAL(60094),
        INVALID_REPORTING_REPORT(60095),
        ERROR_PROCESSING_REPORT(60096),
        PHONE_CHANGE_IN_PROGRESS(60097),
        WEBHOOK_CREATION_ERROR(60098),
        WEBHOOK_LIST_ERROR(60099),
        WEBHOOK_DELETION_ERROR(60100),
        INVALID_JWT_TOKEN(60101),
        PUSH_CERT_CREATION_ERROR(60102),
        NOT_RECOGNIZED_PUSH_PLATFORM(60103),
        INVALID_PUSH_CERTS(60104),
        NOTIFY_JWT_TOKEN_ERROR(60105),
        USER_SUSPENDED_FROM_APP(60106),
        USER_BLOCKED(60107),
        INVALID_CHANNEL_FOR_DEVICE(60108),
        AUTHENTICATION_METHOD_NOT_FOUND(60109),
        AUTHENTICATION_METHOD_CANNOT_BE_CREATED(60110),
        AUTHENTICATION_NOT_FOUND(60111),
        INVALID_AUTHENTICATION_METHOD(60112),
        AUTHENTICATOR_NOT_FOUND(60113),
        AUTHENTICATOR_CANNOT_BE_UPDATED(60114),
        NUMBER_OPTED_OUT(60115),
        BAD_PV_JWT_PARAMS(60116),
        APPLICATION_NOT_FOUND(60117),
        TOTP_CODE_INVALID(60118),
        USER_WITHOUT_PII_REQUIRED(60119),
        SMS_LIMIT_REACHED(60120),
        SMS_INVALID(60121),
        QR_CODE_GENERATION_FAILED(60122),
        GENERIC_TOKENS_DISABLED(60123),
        ACCOUNT_NOT_FOUND(60124),
        INVALID_SDK_APP(60125),
        HLR_REPORT_ERROR(60126),
        USER_PENDING_FOR_DELETION(60127),
        USER_WAS_DELETED(60128),
        PUBLIC_KEY_NOT_FOUND(60129),
        USER_DELETION_ON_GOING(60130),
        USER_DELETION_FAILED(60131),
        ACCOUNT_DELETION_INCOMPLETE(60132),
        CLNPC_MESSAGE(60133) ;

        private final int number;

        Code(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }
    }

    private String message, url, countryCode;
    private Code code;

    @XmlElement(name = "country-code")
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @XmlElement(name = "message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @XmlElement(name = "url")
    public String getUrl() {
        return url;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Map a Token instance to its XML representation.
     *
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xml;
    }

    /**
     * Map a Token instance to its Java's Map representation.
     *
     * @return a Java's Map with the description of this object.
     */
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();

        map.put("message", message);
        map.put("country-code", countryCode);
        map.put("url", url);

        return map;
    }

    @Override
    public String toString() {
        return "Error [message=" + message + ", url=" + url + ", countryCode="
                + countryCode + "]";
    }

}
