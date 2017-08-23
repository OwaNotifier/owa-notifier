# Operating Principle of Owa-Notifier

## Microsoft API credential obtaining

To request MS API, owa-notifier daemon need a oauth2 token from Microsoft. To obtain this oauth2 token, the daemon start :
 * A browser to redirect user on authentication page  
 * A local webserver to handle user after authentication.
 
After a success authentication user is redirected to a local webserver with POST request-data containing a token. The local webserver store this token in memory for future usage and redirect user to OWA.  

![Design OwaNotifier](https://raw.githubusercontent.com/matgou/owa-notifier/master/documentation/Oauth-OwaNotifier.png "Oauth")
