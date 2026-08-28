package us.pushgeni.sdk

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

internal object TokenStore {
    @Volatile var token: String? = null

    fun start() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            token = it
            Client.register()
        }
    }

    fun requestPermission(callback: (Boolean) -> Unit) {
        start()
        callback(true)
    }
}

class PushGeniusMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        TokenStore.token = token
        Client.register()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val id = message.data["notification_id"] ?: return
        Client.track("received", id)
        val click = message.data["click"] == "true"
        if (click) {
            Client.emitClick(NotificationClickEvent(id, message.data))
            Client.track("clicked", id)
        }
    }
}
