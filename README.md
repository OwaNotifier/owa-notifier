# Owa-Notifier
[![Build Status](https://travis-ci.org/OwaNotifier/owa-notifier.svg?branch=master)](https://travis-ci.org/OwaNotifier/owa-notifier)                               
[![codecov.io](https://codecov.io/github/OwaNotifier/owa-notifier/coverage.svg?branch=master)](https://codecov.io/github/OwaNotifier/owa-notifier?branch=master)  

This application display notification when number of unread mail change in a Office365 inbox. It use oauth2 to get graph.microsoft.com API credential.

See [documentation](documentation/OperatingPrinciple.md) for more details about operating principle.

# Screenshot

There is 2 way to display new mail notification with Owa-Notifier :

## On windows 10 using system notification
![screenshot-notification](https://raw.githubusercontent.com/matgou/owa-notifier/master/documentation/screenshot-notification.png "Screenshot Notification")

## On other platform using internal swing notification
![screenshot-notification](https://raw.githubusercontent.com/matgou/owa-notifier/master/documentation/screenshot-swing-notification.png "Screenshot Using Swing Notification")

## Tray icon

When running the application display a icon tray. 

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
