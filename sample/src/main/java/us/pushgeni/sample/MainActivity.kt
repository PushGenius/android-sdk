package us.pushgeni.sample

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import us.pushgeni.sdk.BigBrain
import us.pushgeni.sdk.NotificationClickEvent

class MainActivity : Activity() {
    private lateinit var status: TextView
    private lateinit var ids: TextView
    private lateinit var click: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        status = TextView(this).apply {
            text = if (BuildConfig.PUSHGENIUS_APP_ID.isEmpty()) "Set pushgenius.appId in local.properties" else "PushGenius sample"
        }
        ids = TextView(this).apply { setTextIsSelectable(true) }
        click = TextView(this).apply { setTextIsSelectable(true) }
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 96, 48, 48)
            addView(status)
            addView(Button(this@MainActivity).apply {
                text = "Request permission"
                setOnClickListener {
                    BigBrain.Notifications.requestPermission(true) { granted ->
                        status.text = if (granted) "Permission granted" else "Denied"
                    }
                }
            })
            addView(Button(this@MainActivity).apply {
                text = "Login user-1"
                setOnClickListener { BigBrain.login("user-1") }
            })
            addView(Button(this@MainActivity).apply {
                text = "Tag plan=pro"
                setOnClickListener { BigBrain.User.addTag("plan", "pro") }
            })
            addView(Button(this@MainActivity).apply {
                text = "Refresh IDs"
                setOnClickListener { showIds() }
            })
            addView(ids)
            addView(click)
        }
        setContentView(layout)
    }

    override fun onResume() {
        super.onResume()
        showIds()
        SampleApp.lastClick?.let(::showClick)
        SampleApp.onClick = { runOnUiThread { showClick(it) } }
    }

    override fun onPause() {
        super.onPause()
        SampleApp.onClick = null
    }

    private fun showIds() {
        ids.text = """
            |
            |External ID: ${BigBrain.User.externalId ?: "-"}
            |Subscription: ${BigBrain.User.pushSubscription.id ?: "-"}
            |Opted in: ${BigBrain.User.pushSubscription.optedIn}
            |Token: ${BigBrain.User.pushSubscription.token?.take(16)?.plus("…") ?: "-"}
        """.trimMargin()
    }

    private fun showClick(event: NotificationClickEvent) {
        click.text = "\nLast tap: ${event.notificationId}\nData: ${event.additionalData}"
    }
}
