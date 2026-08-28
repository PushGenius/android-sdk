# pushgenius-android

Standalone Android library. OneSignal-shaped API for FCM.

```kotlin
PushGenius.setHost("https://api.pushgeni.us")
OneSignal.initialize(APP_ID)
OneSignal.Notifications.requestPermission(true)
OneSignal.login("user-123")
OneSignal.User.addTag("plan", "pro")
```

Publish to Maven (or include as a git submodule / composite build). Host apps need `google-services.json` and the Google Services plugin.

Sample: `sample/`.
