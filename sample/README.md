# PushGenius sample app

An Android app using the SDK from this repo, for testing pushes on a real phone.

## Set up

1. In Firebase, add an Android app with package name `us.pushgeni.sample` and download its `google-services.json` into this folder. It isn't committed. To use another package name, set `pushgenius.applicationId` below.
2. In the dashboard, save that Firebase project's service account under **Settings → Android**.
3. Add your app ID to `local.properties` in the repo root (not committed):
   ```properties
   pushgenius.appId=YOUR_APP_ID
   # Optional
   # pushgenius.applicationId=com.example.pushgenius-test
   # pushgenius.host=http://192.168.1.10:3001
   ```
4. Install on a phone: `./gradlew :sample:installDebug`, or run the `sample` configuration from Android Studio.
5. In the app, tap **Request permission**, then **Login user-1**, then **Refresh IDs**. A subscription ID means the phone is registered.

## Test

Send a test to the logged-in user:

```sh
curl -X POST https://api.pushgeni.us/notifications \
  -H "Authorization: Key YOUR_REST_API_KEY" -H "Content-Type: application/json" \
  -d '{"app_id":"YOUR_APP_ID","headings":{"en":"Android swiped 1"},"contents":{"en":"test"},"data":{"order_id":"42"},"include_aliases":{"external_id":["user-1"]}}'
```

Send one with the app open, one in the background, and one after swiping it away from recents. Each should show a bell icon, and its counts on the dashboard's Messages page should show Received 1, and Clicked 1 once tapped. The app shows the last tap's ID and data.

A force-stopped app (**Settings → Apps → Force stop**) gets nothing until it's opened again; Android blocks it. If a swiped-away app gets nothing, the phone's battery saver may be holding messages; set the app's battery use to **Unrestricted** and try again.
