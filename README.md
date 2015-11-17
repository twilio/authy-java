# Authy

Java library to access the Authy API

## Compilation

### Using Apache Ant

Use `ant` to generate `authy-java.jar` or use the jar file in `dist` directory.

	ant compress
	
This will generate a jar file in `dist` folder.

### Using Apache Maven

Use `mvn install` to compile and install the library in your local maven repository. This also installs the sources JAR file for source code lookup for better debugging.

You will also find the JAR files in the `target` directory.

## Usage

Import API:

	import com.authy.*;
	import com.authy.api.*;
	
Create an API instance:

	AuthyApiClient client = new AuthyApiClient("your-api-key", "https://api.authy.com/");

or

	AuthyApiClient client = new AuthyApiClient("your-api-key");

Get a Users and Tokens instance:

	Users users = client.getUsers();
	Tokens tokens = client.getTokens();

## Registering a user

__NOTE: User is matched based on cellphone and country code not e-mail.
A cellphone is uniquely associated with an authy_id.__  


`users.createUser(String, String)` requires the user's e-mail address and cellphone. Optionally you can pass in the countryCode or we will assume USA. The call will return the authy id for the user that you need to store in your database.

To create a user just use:

	User user = users.createUser("new_user@email.com", "405-342-5699", "57");
	// createUser takes as arguments the email, phone number and country code

You can check if the user was created calling `user.isOk()`.
If the request was successful, you will need to store the authy id in your database. Use `user.getId()` to get this `id` in your database.

	if(user.isOk())
	// Store user.getId() in your database

If something goes wrong `user.isOk()` returns `false` and you can see the errors using the following code

	user.getError();
	
It returns an Error object explaining what went wrong with the request.

## Verifying a user


__NOTE: Token verification is only enforced if the user has completed registration. To change this behaviour see Forcing Verification section below.__  
   
   >*Registration is completed once the user installs and registers the Authy mobile app or logins once successfully using SMS.*

`tokens.verify()` takes the authy_id that you are verifying and the token that you want to verify. You should have the authy_id in your database

    Token verification = tokens.verify(authy_id, "token-user-entered");

Once again you can use `isOk()` to verify whether the token was valid or not.

    if(verification.isOk())
		// token was valid, user can sign in
    else
		// token is invalid

In case `verification.isOk()` returns `false`, you can get an Error object using `verification.getError()`

### Forcing Verification

If you wish to verify tokens even if the user has not yet complete registration, pass force=true when verifying the token.

	Map<String, String> options = new HashMap<String, String>();
	options.put("force", "true");
    Token verification = tokens.verify(authy_id, "token-user-entered", options);

## Deleting a user

`users.deleteUser()` takes the authy_id that you want to delete. You should have the authy_id in your database

	Hash response = users.deleteUser();

Once again you can use `isOk()` to verify whether the user was deleted or not.

	if(response.isOk())
		// User was deleted
	else
		// Some error ocurred

In case `response.isOk()` returns `false`, you can get an Error object using `response.getError()`

## Requesting a SMS token

`users.requestSms()` takes the authy_id that you want to send a SMS token. This requires Authy SMS plugin to be enabled.

    Hash sms = users.requestSms(authy_id);
	
As always, you can use `isOk()` to verify if the token was sent.

    if(sms.isOk())
		// sms was sent

In case `sms.isOk()` returns `false`, you can get an Error object using `sms.getError()`

This call will be ignored if the user is using the Authy Mobile App. If you still want to send
the SMS pass force=true as an option

	Map<String, String> options = new HashMap<String, String>();
	options.put("force", "true");
	Hash sms = users.requestSms(authy_id, options);

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

Copyright (c) 2013 Authy Inc. See LICENSE.txt for
further details.
