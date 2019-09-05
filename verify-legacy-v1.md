# Phone Verification V1

[Version 2 of the Verify API is now available!](https://www.twilio.com/docs/verify/api) V2 has an improved developer experience and new features. Some of the features of the V2 API include:

* Twilio helper libraries in JavaScript, Java, C#, Python, Ruby, and PHP
* PSD2 Secure Customer Authentication Support
* Improved Visibility and Insights

**You are currently viewing Version 1. V1 of the API will be maintained for the time being, but any new features and development will be on Version 2. We strongly encourage you to do any new development with API V2.** Check out the [migration guide](https://www.twilio.com/docs/verify/api/migrating-1x-2x) or the API Reference for more information.

### API Reference

API Reference is available at https://www.twilio.com/docs/verify/api/v1

### Sending the verification code.

  ```java
  AuthyApiClient client = new AuthyApiClient("SomeApiKey");
  PhoneVerification phoneVerification  = client.getPhoneVerification();

  Verification verification;
  Params params = new Params();
  params.setAttribute("locale", en);

  verification = phoneVerification.start("111-111-1111", "1", "sms", params);

  System.out.println(verification.getMessage());
  System.out.println(verification.getIsPorted());
  System.out.println(verification.getSuccess());
  System.out.println(verification.isOk());
  ```

### Check the verification code.
  Once you sent the verification code the user will receive the code in the
  mobile device. Then you need to provide this code to check if it is okay.


  ```java
  AuthyApiClient client = new AuthyApiClient("SomeApiKey");
  PhoneVerification phoneVerification = client.getPhoneVerification();

  Verification verification;
  verification = phoneVerification.check("111-111-1111", "1", "2061");

  System.out.println(verificationCode.getMessage());
  System.out.println(verificationCode.getIsPorted());
  System.out.println(verificationCode.getSuccess());
  ```