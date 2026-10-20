# pushgenius-android

Standalone Android library. `BigBrain` API for FCM.

```kotlin
PushGenius.setHost("https://api.pushgeni.us")
BigBrain.initialize(APP_ID)
BigBrain.Notifications.requestPermission(true)
BigBrain.login("user-123")
BigBrain.User.addTag("plan", "pro")
```

Publish to Maven (or include as a git submodule / composite build). Host apps need `google-services.json` and the Google Services plugin.

Sample: `sample/`.
