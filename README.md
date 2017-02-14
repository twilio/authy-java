# Authy

Java library to access the Authy API

## Dependencies

### JSON Library
This project use [json.org](https://github.com/douglascrockford/JSON-java) you can find
the [json.org jar versions here](https://search.maven.org/#search|gav|1|g%3A%22org.json%22%20AND%20a%3A%22json%22)

* Using Ant you don't need to include json.org since ant is including it in the final jar.

* Using Maven you need to include the [json.org jar](https://search.maven.org/#search|gav|1|g%3A%22org.json%22%20AND%20a%3A%22json%22) in your jar external libraries.

## Compilation


### Using Apache Ant

Use `ant` to generate `authy-java.jar` or use the jar file in `dist` directory.

ant compress

This will generate a jar file in `dist` folder.

If you want to use this command:
```shell
$ ant compress
```
### Using Apache Maven

Use `mvn install` to compile and install the library in your local maven repository. This also installs the sources JAR file for source code lookup for better debugging.

You will also find the JAR files in the `target` directory.

Also you can use Use `mvn test` if you want to run tests.

## Usage

Import API:
```java
import com.authy.*;
import com.authy.api.*;
```

Create an API instance:

```java
AuthyApiClient client = new AuthyApiClient("your-api-key", "https://api.authy.com/");
```

or by default api url is `https://api.authy.com/`

```java
AuthyApiClient client = new AuthyApiClient("your-api-key");
```

or if you want to use in local mode or for debugging purpose

```java
AuthyApiClient client = new AuthyApiClient("your-api-key");
String apiKey = "bf12974d70818a08199d17d5e2bae630";
String apiUrl = "http://sandbox-api.authy.com";
boolean debugMode = true;

AuthyApiClient client = new AuthyApiClient(apiKey, apiUrl, debugMode);
```

Get a Users and Tokens instance:
```java
Users users = client.getUsers();
Tokens tokens = client.getTokens();
```

## Registering a user

__NOTE: User is matched based on cellphone and country code not e-mail.
A cellphone is uniquely associated with an authy_id.__


`users.createUser(String, String)` requires the user's e-mail address and cellphone. Optionally you can pass in the countryCode or we will assume USA. The call will return the authy id for the user that you need to store in your database.

To create a user just use:
```java
User user = users.createUser("new_user@email.com", "405-342-5699", "57");
// createUser takes as arguments the email, phone number and country code
```

You can check if the user was created calling `user.isOk()`.
If the request was successful, you will need to store the authy id in your database. Use `user.getId()` to get this `id` in your database.

```java

  if(user.isOk()){
    // Store user.getId() in your database
  }
```
  If something goes wrong `user.isOk()` returns `false` and you can see the errors using the following code

```java
  user.getError();
```

  It returns an Error object explaining what went wrong with the request.

## Verifying a user


  __NOTE: Token verification is only enforced if the user has completed registration. To change this behaviour see Forcing Verification section below.__

  >*Registration is completed once the user installs and registers the Authy mobile app or logins once successfully using SMS.*

  `tokens.verify()` takes the authy_id that you are verifying and the token that you want to verify. You should have the authy_id in your database

  Token verification = tokens.verify(authy_id, "token-user-entered");

  Once again you can use `isOk()` to verify whether the token was valid or not.

```java
  if(verification.isOk()){
  // token was valid, user can sign in
  }else{
    // token is invalid
  }
```
  In case `verification.isOk()` returns `false`, you can get an Error object using `verification.getError()`

### Forcing Verification

  If you wish to verify tokens even if the user has not yet complete registration, pass force=true when verifying the token.
```java
  Map<String, String> options = new HashMap<String, String>();
  options.put("force", "true");
  Token verification = tokens.verify(authy_id, "token-user-entered", options);
```

## Deleting a user

  `users.deleteUser()` takes the authy_id that you want to delete. You should have the authy_id in your database

```java
  Hash response = users.deleteUser();
```

  Once again you can use `isOk()` to verify whether the user was deleted or not.
```java
  if(response.isOk()){
    // User was deleted
  }else{
    // Some error ocurred
  }
```

  In case `response.isOk()` returns `false`, you can get an Error object using `response.getError()`

## Requesting a SMS token

  `users.requestSms()` takes the authy_id that you want to send a SMS token. This requires Authy SMS plugin to be enabled.

```java
  Hash sms = users.requestSms(authy_id);
```

  As always, you can use `isOk()` to verify if the token was sent.

```java
  if(sms.isOk())
  // sms was sent ;
```

  In case `sms.isOk()` returns `false`, you can get an Error object using `sms.getError()`

  This call will be ignored if the user is using the Authy Mobile App. If you still want to send
  the SMS pass force=true as an option

```java
  Map<String, String> options = new HashMap<String, String>();
  options.put("force", "true");
  Hash sms = users.requestSms(authy_id, options);
```

## OneTouch Support

  `client.getOneTouch()` will return the OneTouch wrapper, with this component you will be able to use all the 
  endpoints defined in the [OneTouch API](https://www.twilio.com/docs/api/authy/authy-onetouch-api), 
  for example, using the 
  'sendApprovalRequest' method you will create an ApprovalRequest
  in OneTouch, then the approval request
  status can be queried by using the  `uuid` provided. (NOTE: The UUID is the unique identifier for the request sent to OneTouch)    

```java
//Detail and hidden details information are required
//The maximum length of values for each key is 200
   OneTouchOptionParams options = new OneTouchOptionParams();
        options.addDetail("username", "User");
        options.addDetail("location", "California,USA");
        options.addHiddenDetails("ip_address", "10.10.3.203");
        options.addLogo(Logo.Resolution.Low, "https://myurlimage.co");

    OneTouchResponse response = client.getOneTouch().sendApprovalRequest(Integer.parseInt(103), "Authorize OneTouch Unit Test", options.generateParams(), ,86400);
    // If the request was successfuly created.
    assert response.isSuccess();
    String uuid = response.getApprovalRequest().getUUID();
    System.out.println(uuid);
```

  If you want to query the ApprovalRequest status
```java
  String status = client.getOneTouch().getApprovalRequestStatus(uuid).getStatus();
  System.out.println(status);
```
When you make a OneTouch request, you will get a OneTouchResponse object which in turn will have a getApprovalRequest() accessor to retrieve the metadata from the given request, for example:

```java
    OneTouchResponse response = client.getOneTouch().getApprovalRequestStatus(uuid);
    ApprovalRequest approvalRequest  = response.getApprovalRequest();
    // was the user notified?
    System.out.println("The user was "+(approvalRequest.isNotified()?"":"not")+" notified.");
    // What is the status of the request?
    System.out.print("The status of the request is: "+approvalRequest.getStatus());

```
As described in the documentation, you can define a callback from Authy to your web application were you will receive the result of your request with all the related information.

After you define your callback, if you want to keep up with following our *Developer Best Practices* you should [Verify the authenticity of callbacks from Authy](https://www.twilio.com/docs/api/authy/authy-onetouch-api#verifying-authenticity-of-callbacks-from-authy) all this process can be done easily just by using an utility method from *AuthyUtil* like this:

Inside your app your app will get a request into a servlet/controller were you will have the Http Request object.
```java
    // let's create a Map of Strings to put the query parameters from the Http Request
    HashMap<String,String> params = new HashMap<>();
    for(String p : request.queryParams()){
        params.put(p,request.queryParams(p));
    }
    
    // let's create a Map of Strings to put the query headers from the Http Request
    HashMap<String,String> headers = new HashMap<>();
    for(String h : request.headers()){
        headers.put(h,request.headers(h));
    }

    //Now it is really easy just to ask AuthyUtil to check the authenticity of the signature, TOKEN will be the string with your Api_key
    // AuthyUtil.validateSignature will tell you if the request is secure or not!
    boolean isValid = AuthyUtil.validateSignature(params, headers, request.requestMethod(), request.url(), TOKEN)        
     
     // Then you can just finish the rewquest with the proper HTTP code or do any other procesing you need for this case. 
     if(!isValid){
         response.status(401);
         return "Unauthorized";
     }

```     


## Phone Verification

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

## Phone Intelligence
  Getting more info about a phone number.

  ```java
  AuthyApiClient client = new AuthyApiClient("SomeApiKey");
  PhoneInfo phoneInfo  = client.getPhoneInfo();
  PhoneInfoResponse phoneInfoResponse;

  phoneInfoResponse = phoneInfo.info("111-111-1111", "1");

  System.out.println(phoneInfoResponse.getMessage());
  System.out.println(phoneInfoResponse.getProvider());
  System.out.println(phoneInfoResponse.getSuccess());
  System.out.println(phoneInfoResponse.getIsPorted());
  System.out.println(phoneInfoResponse.getType());
  ```

## More...

  You can get an XML representation of some objects. See javadoc documentation.

  You can find the full API documentation in the [official documentation](https://docs.authy.com) page.

### Contributing to authy

  * Check out the latest master to make sure the feature hasn't been implemented or the bug hasn't been fixed yet.
  * Check out the issue tracker to make sure someone already hasn't requested it and/or contributed it.
  * Fork the project.
  * Start a feature/bugfix branch.
  * Commit and push until you are happy with your contribution.
  * Make sure to add tests for it. This is important so I don't break it in a future version unintentionally.
  * Please try not to mess with the Rakefile, version, or history. If you want to have your own version, or is otherwise necessary, that is fine, but please isolate to its own commit so I can cherry-pick around it.

  Contributors
  ==
  * Bhagya Silva [http://about.me/bhagyas](http://about.me/bhagyas)


  Copyright
  ==

  Copyright (c) 2011-2020 Authy Inc. See LICENSE.txt for
  further details.
