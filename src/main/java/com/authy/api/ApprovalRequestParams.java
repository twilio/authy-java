/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.authy.api;

import com.authy.OneTouchException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author hansospina
 *         <p>
 *         Copyright © 2017 Twilio, Inc. All Rights Reserved.
 */
public class ApprovalRequestParams {

    private HashMap<String, String> details = new HashMap<String, String>();
    private HashMap<String, String> hidden = new HashMap<String, String>();
    private List<Logo> logos = new ArrayList<>();
    private Long secondsToExpire;
    private Integer authyId;
    private String message;

    // lock the main constructor
    private ApprovalRequestParams() {
    }

    public HashMap<String, String> getDetails() {
        return details;
    }

    public HashMap<String, String> getHidden() {
        return hidden;
    }

    public List<Logo> getLogos() {
        return logos;
    }

    public Long getSecondsToExpire() {
        return secondsToExpire;
    }

    public Integer getAuthyId() {
        return authyId;
    }

    public String getMessage() {
        return message;
    }

    public enum Resolution {
        Default("default"),
        Low("low"),
        Medium("med"),
        High("high");

        private String res;

        Resolution(String res) {
            this.res = res;
        }

        public String getRes() {
            return res;
        }


    }

    public static class Builder {


        public static final String MESSAGE_ERROR = "Param message cannot be null or empty  and it's length needs to be less than 200 max characters.";
        public static final String AUTHYID_ERROR = "Param authyId cannot be null and should be the id of the user that will be authorized using OneTouch.";
        public static final String DETAIL_ERROR = "Each entry(key,value) for a detail needs to have not null or empty values and keys and it's lengths cannot exceed 200 max characters.";
        public static final String HIDDEN_DETAIL_ERROR = "Each entry(key,value) for a hidden detail needs to have not null or empty values and keys and it's lengths cannot exceed 200 max characters.";
        public static final String LOGO_ERROR_RES = "The 'Resolution' for a logo cannot be null.";
        public static final String LOGO_ERROR_URL = "The 'url' for a logo cannot be null or empty  and it's length needs to be less than 500 max characters.";
        public static final String LOGO_ERROR_DEFAULT = "If you provide logos you should always provide the default Resolution.";
        private static final int MAXSIZE = 200;
        private static final int MAXSIZEURL = 500;
        ApprovalRequestParams params = new ApprovalRequestParams();
        private HashMap<Resolution, Logo> currentLogos = new HashMap<>();

        /**
         * This will create a valid builder with the initial required params.
         * See: https://www.twilio.com/docs/api/authy/authy-onetouch-api#parameters
         *
         * @param authyId The  id of the user that will be authorized using OneTouch
         * @param message The message to show to the user, cannot be null or empty and it's length needs to be less than 200 max characters.
         * @throws OneTouchException If any of the params doesn't match the required/length rules.
         */
        public Builder(Integer authyId, String message) throws OneTouchException {

            if (authyId == null) {
                throw new OneTouchException(AUTHYID_ERROR);
            }

            if (message == null || message.isEmpty() || message.length() > MAXSIZE) {
                throw new OneTouchException(MESSAGE_ERROR);
            }

            this.params.authyId = authyId;
            this.params.message = message;
        }

        /**
         * Defines the second to expire parameter
         * <p>
         * See: https://www.twilio.com/docs/api/authy/authy-onetouch-api#parameters
         *
         * @param secondsToExpire Number of seconds that the approval request will be available for being responded.
         */
        public Builder setSecondsToExpire(Long secondsToExpire) {
            // this parameter can be null or any long value so we don't need to validate anything else.
            this.params.secondsToExpire = secondsToExpire;
            return this;
        }

        /**
         * Adds a key,value pair into the details which is a Dictionary containing the [ApprovalRequest] details that will be shown to user.
         * <p>
         * See: https://www.twilio.com/docs/api/authy/authy-onetouch-api#parameters
         *
         * @param key   The label of the detail that will be shown to the user, cannot be null or empty and it's length needs to be less than 200 max characters.
         * @param value The value of the detail that will be shown to the user, cannot be null or empty and it's length needs to be less than 200 max characters.
         * @throws OneTouchException If any of the params doesn't match the required/length rules.
         */
        public Builder addDetail(String key, String value) throws OneTouchException {

            if (key == null || key.isEmpty() || key.length() > MAXSIZE) {
                throw new OneTouchException(DETAIL_ERROR);
            }

            if (value == null || value.isEmpty() || value.length() > MAXSIZE) {
                throw new OneTouchException(DETAIL_ERROR);
            }

            this.params.details.put(key, value);

            return this;
        }

        /**
         * Adds a key,value pair into the hidden_details which is a Dictionary containing the approval request details hidden to user.
         * <p>
         * See: https://www.twilio.com/docs/api/authy/authy-onetouch-api#parameters
         *
         * @param key   The label of the hidden detail that will be not shown to the user, cannot be null or empty and it's length needs to be less than 200 max characters.
         * @param value The value of the hidden detail that will be not shown to the user, cannot be null or empty and it's length needs to be less than 200 max characters.
         * @throws OneTouchException If any of the params doesn't match the required/length rules.
         */
        public Builder addHiddenDetail(String key, String value) throws OneTouchException {

            if (key == null || key.isEmpty() || key.length() > MAXSIZE) {
                throw new OneTouchException(HIDDEN_DETAIL_ERROR);
            }

            if (value == null || value.isEmpty() || value.length() > MAXSIZE) {
                throw new OneTouchException(HIDDEN_DETAIL_ERROR);
            }

            this.params.hidden.put(key, value);

            return this;
        }


        /**
         * Adds a new logo  item to the Logos array.
         *
         * @param resolution The Resolution wanted for the given logo(Default, log,med,high)
         * @param url        The url to be used to load the logo.
         * @throws OneTouchException If any of the params doesn't match the required/length rules.
         */
        public Builder addLogo(Resolution resolution, String url) throws OneTouchException {

            if (resolution == null) {
                throw new OneTouchException(LOGO_ERROR_RES);
            }

            if (url == null || url.isEmpty() || url.length() > MAXSIZEURL) {
                throw new OneTouchException(LOGO_ERROR_URL);
            }

            // let's use a map to prevent duplicates in the logo list,
            // if a new logo with an already used resolution comes we will just replace it.
            this.currentLogos.put(resolution, new Logo(resolution, url));
            return this;
        }


        /**
         * Compiles and creates the provided set of parameters to have a ready to use ApprovalRequestParams object.
         *
         * @return The bean containing all the properties required to send a valid OneTouch request to Authy.
         * @throws OneTouchException If any of the params doesn't match the required/length rules.
         */
        public ApprovalRequestParams build() throws OneTouchException {

            // if we have logo but the user didnt send a default this
            if (!currentLogos.isEmpty() && !currentLogos.containsKey(Resolution.Default)) {
                throw new OneTouchException(LOGO_ERROR_DEFAULT);
            }

            this.params.logos.addAll(currentLogos.values());


            return this.params;
        }


    }


}
