# pushgenius-android

Standalone Android library. `BigBrain` API for FCM.

```kotlin
PushGenius.setHost("https://api.pushgeni.us")
BigBrain.initialize(APP_ID)
BigBrain.Notifications.requestPermission(true)
BigBrain.login("user-123")
BigBrain.User.addTag("plan", "pro")
```

Install from [JitPack](https://jitpack.io): add `maven { url = uri("https://jitpack.io") }` to your repositories, then

```kotlin
implementation("com.github.PushGenius:android-sdk:0.2.0")
```

Releases are git tags (`git tag 0.2.0 && git push origin 0.2.0`); JitPack builds the tag on first request using `jitpack.yml`. Host apps need `google-services.json` and the Google Services plugin.

`requestPermission` shows the Android 13+ notification prompt; once the user has denied it twice, `fallbackToSettings = true` opens the app's notification settings instead. PushGenius sends data-only messages, so the SDK displays every notification itself, whether the app is open, in the background, or closed, and reports each one as received. Set a monochrome small icon with the `com.google.firebase.messaging.default_notification_icon` meta-data in your manifest; otherwise the app icon is used. The SDK keeps the user's IDs across launches and reports taps to `Notifications.addClickListener` (the server sends taps to the SDK's `NotificationOpenedActivity`, which then opens your launcher activity with the notification data as extras).

Build locally with `./gradlew :pushgenius:assembleRelease`.

Sample app for testing on a phone: [`sample/`](sample/README.md).
