[![Build Status](https://travis-ci.org/twilio/authy-java.svg?branch=master)](https://travis-ci.org/twilio/authy-java)
[![codecov.io](http://codecov.io/github/twilio/authy-java/coverage.svg?branch=master)](https://codecov.io/gh/twilio/authy-java)

🚨🚨🚨

**This library is no longer actively maintained.** The Authy API has been replaced with the [Twilio Verify API](https://www.twilio.com/docs/verify). Twilio will support the Authy API through November 1, 2022 for SMS/Voice. After this date, we’ll start to deprecate the service for SMS/Voice. Any requests sent to the API after May 1, 2023, will automatically receive an error.  Push and TOTP will continue to be supported through July 2023.

[Learn more about migrating from Authy to Verify.](https://www.twilio.com/blog/migrate-authy-to-verify)

Please visit the Twilio Docs for:
* [Verify + Java (Servlets) quickstart](https://www.twilio.com/docs/verify/quickstarts/java-servlets)
* [Twilio Java helper library](https://www.twilio.com/docs/libraries/java)
* [Verify API reference](https://www.twilio.com/docs/verify/api)

Please direct any questions to [Twilio Support](https://support.twilio.com/hc/en-us). Thank you!

🚨🚨🚨


## Java Client for Twilio Authy Two-Factor Authentication (2FA) API

Documentation for Java usage of the Authy API lives in the [official Twilio documentation](https://www.twilio.com/docs/authy/api/).

The Authy API supports multiple channels of 2FA:
* One-time passwords via SMS and voice.
* Soft token ([TOTP](https://www.twilio.com/docs/glossary/totp) via the Authy App)
* Push authentication via the Authy App

If you only need SMS and Voice support for one-time passwords, we recommend using the [Twilio Verify API](https://www.twilio.com/docs/verify/api) instead. 

[More on how to choose between Authy and Verify here.](https://www.twilio.com/docs/verify/authy-vs-verify)

### Authy Quickstart

For a full tutorial, check out either of the Java Authy Quickstarts in our docs:
* [Java/Spring Authy Quickstart](https://www.twilio.com/docs/authy/quickstart/two-factor-authentication-java-spring)
* [Java/Servlets Authy Quickstart](https://www.twilio.com/docs/authy/quickstart/two-factor-authentication-java-servlets)

## Authy Java Installation

### Dependencies
This project uses [json.org](https://github.com/douglascrockford/JSON-java), you can find
the [json.org jar versions here](https://search.maven.org/#search|gav|1|g%3A%22org.json%22%20AND%20a%3A%22json%22)

* **Ant:** no need to include json.org since ant already includes it in the final jar.
* **Maven:** need to include the [json.org jar](https://search.maven.org/#search|gav|1|g%3A%22org.json%22%20AND%20a%3A%22json%22) in your jar external libraries.

## Usage

Add the library to the project by putting it in the dependencies section of your `pom.xml`:
```
<!-- https://mvnrepository.com/artifact/com.authy/authy-java -->
<dependency>
    <groupId>com.authy</groupId>
    <artifactId>authy-java</artifactId>
    <version>1.5.0</version>
</dependency>
```

To use the Authy client, import the API and initialize it with your production API Key found in the [Twilio Console](https://www.twilio.com/console/authy/applications/):

```java
import com.authy.*;
import com.authy.api.*;

AuthyApiClient client = new AuthyApiClient("your-api-key")
```

![authy api key in console](https://s3.amazonaws.com/com.twilio.prod.twilio-docs/images/account-security-api-key.width-800.png)

## 2FA Workflow

1. [Create a user](https://www.twilio.com/docs/authy/api/users#enabling-new-user)
2. [Send a one-time password](https://www.twilio.com/docs/authy/api/one-time-passwords)
3. [Verify a one-time password](https://www.twilio.com/docs/authy/api/one-time-passwords#verify-a-one-time-password)

**OR**

1. [Create a user](https://www.twilio.com/docs/authy/api/users#enabling-new-user)
2. [Send a push authentication](https://www.twilio.com/docs/authy/api/push-authentications)
3. [Check a push authentication status](https://www.twilio.com/docs/authy/api/push-authentications#check-approval-request-status)


## <a name="phone-verification"></a>Phone Verification

[Phone verification now lives in the Twilio API](https://www.twilio.com/docs/verify/api) and has [Java support through the official Twilio helper libraries](https://www.twilio.com/docs/libraries/java). 

[Legacy (V1) documentation here.](verify-legacy-v1.md) **Verify V1 is not recommended for new development. Please consider using [Verify V2](https://www.twilio.com/docs/verify/api)**.

## Copyright

Copyright (c) 2011-2020 Authy Inc. See [LICENSE](https://github.com/twilio/authy-java/blob/master/LICENSE.txt) for further details.
