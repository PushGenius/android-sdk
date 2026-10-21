package us.pushgeni.sample

import android.app.Application
import us.pushgeni.sdk.BigBrain
import us.pushgeni.sdk.NotificationClickEvent

class SampleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.PUSHGENIUS_HOST.isNotEmpty()) BigBrain.setHost(BuildConfig.PUSHGENIUS_HOST)
        BigBrain.initialize(BuildConfig.PUSHGENIUS_APP_ID)
        // Registered here rather than in an activity so the tap that cold-starts the app is delivered too.
        BigBrain.Notifications.addClickListener { lastClick = it; onClick?.invoke(it) }
    }

    companion object {
        var lastClick: NotificationClickEvent? = null
        var onClick: ((NotificationClickEvent) -> Unit)? = null
    }
}
