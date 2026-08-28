package us.pushgeni.sample

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import us.pushgeni.sdk.OneSignal
import us.pushgeni.sdk.PushGenius

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PushGenius.setHost("http://192.168.1.10:3001")
        OneSignal.initialize("YOUR_APP_ID")

        val status = TextView(this).apply { text = "PushGenius sample" }
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 48, 48, 48)
            addView(status)
            addView(Button(this@MainActivity).apply {
                text = "Request permission"
                setOnClickListener {
                    OneSignal.Notifications.requestPermission(true) { granted ->
                        status.text = if (granted) "Permission granted" else "Denied"
                    }
                }
            })
            addView(Button(this@MainActivity).apply {
                text = "Login user-1"
                setOnClickListener { OneSignal.login("user-1") }
            })
            addView(Button(this@MainActivity).apply {
                text = "Tag plan=pro"
                setOnClickListener { OneSignal.User.addTag("plan", "pro") }
            })
        }
        setContentView(layout)
    }
}
