package us.pushgeni.sdk

import android.os.Handler
import android.os.Looper
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executors

internal object Client {
    @Volatile var onesignalId: String? = null
    @Volatile var externalId: String? = null
    @Volatile var subscriptionId: String? = null
    @Volatile var optedIn: Boolean = true
    val clickListeners = CopyOnWriteArrayList<(NotificationClickEvent) -> Unit>()
    private val io = Executors.newSingleThreadExecutor()
    private val main = Handler(Looper.getMainLooper())

    fun register() {
        io.execute {
            val subs = JSONArray()
            TokenStore.token?.let { token ->
                subs.put(JSONObject().apply {
                    put("type", "AndroidPush")
                    put("token", token)
                    put("enabled", optedIn)
                    put("notification_types", if (optedIn) 1 else -2)
                    put("sdk", "pushgenius/0.1.0")
                    put("device_os", "Android")
                })
            }
            val identity = JSONObject()
            externalId?.let { identity.put("external_id", it) }
            onesignalId?.let { identity.put("onesignal_id", it) }
            val body = JSONObject()
                .put("identity", identity)
                .put("subscriptions", subs)
            apply(post("/sdk/apps/${PushGenius.appId}/users", body))
        }
    }

    fun login(externalId: String) {
        this.externalId = externalId
        io.execute {
            val body = JSONObject().put("external_id", externalId)
            subscriptionId?.let { body.put("subscription_id", it) }
            apply(post("/sdk/apps/${PushGenius.appId}/login", body))
        }
    }

    fun logout() {
        externalId = null
        onesignalId = null
        register()
    }

    fun patch(properties: Map<String, Any>? = null, identity: Map<String, String>? = null) {
        io.execute {
            val id = onesignalId ?: return@execute register()
            val body = JSONObject()
            properties?.let { body.put("properties", JSONObject(it)) }
            identity?.let { body.put("identity", JSONObject(it)) }
            apply(request("PATCH", "/sdk/apps/${PushGenius.appId}/users/by/onesignal_id/$id", body))
        }
    }

    fun setPermission(granted: Boolean) {
        optedIn = granted
        io.execute {
            subscriptionId?.let {
                post(
                    "/sdk/apps/${PushGenius.appId}/events",
                    JSONObject()
                        .put("event", "permission")
                        .put("subscription_id", it)
                        .put("permission", granted),
                )
            }
            register()
        }
    }

    fun track(event: String, notificationId: String) {
        io.execute {
            val sub = subscriptionId ?: return@execute
            post(
                "/sdk/apps/${PushGenius.appId}/events",
                JSONObject()
                    .put("event", event)
                    .put("notification_id", notificationId)
                    .put("subscription_id", sub),
            )
        }
    }

    fun emitClick(event: NotificationClickEvent) {
        main.post { clickListeners.forEach { it(event) } }
    }

    private fun apply(json: JSONObject?) {
        json ?: return
        json.optJSONObject("identity")?.let {
            onesignalId = it.optString("onesignal_id", onesignalId ?: "")
            if (it.has("external_id")) externalId = it.optString("external_id")
        }
        json.optJSONArray("subscriptions")?.optJSONObject(0)?.let {
            subscriptionId = it.optString("id")
            optedIn = it.optBoolean("enabled", true)
        }
    }

    private fun post(path: String, body: JSONObject) = request("POST", path, body)

    private fun request(method: String, path: String, body: JSONObject): JSONObject? {
        return try {
            val conn = URL(PushGenius.host + path).openConnection() as HttpURLConnection
            conn.requestMethod = method
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true
            conn.outputStream.use { it.write(body.toString().toByteArray()) }
            conn.inputStream.bufferedReader().use { JSONObject(it.readText()) }
        } catch (_: Exception) {
            null
        }
    }
}
