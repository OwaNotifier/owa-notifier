# Owa-Notifier
[![Build Status](https://travis-ci.org/matgou/owa-notifier.svg?branch=master)](https://travis-ci.org/matgou/owa-notifier)                               
[![codecov.io](https://codecov.io/github/matgou/owa-notifier/coverage.svg?branch=master)](https://codecov.io/github/matgou/owa-notifier?branch=master)  

This application display notification when number of unread mail change in a Office365 inbox. It use oauth2 to get graph.microsoft.com API credential.


# Screenshot
## On windows 10 using system notification
![screenshot-notification](https://raw.githubusercontent.com/matgou/owa-notifier/master/documentation/screenshot-notification.png "Screenshot Notification")

## On other platform using internal swing notification
![screenshot-notification](https://raw.githubusercontent.com/matgou/owa-notifier/master/documentation/screenshot-swing-notification.png "Screenshot Using Swing Notification")

## Tray icon
![screenshot-tray](https://raw.githubusercontent.com/matgou/owa-notifier/master/documentation/screenshot-tray.png "Screenshot tray-icon")

# Minimum requirement :
 * java 1.6
 * Desktop environnement
 * A browser
 * Linux or Windows
 
# Build 

```bash
mvn clean package
```

# Run

```bash
java -jar target\OwaNotifier-jar-with-dependencies.jar
```