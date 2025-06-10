package com.fz.microviewerapp;

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import java.lang.Thread.sleep
import java.net.URL;
import java.util.Hashtable

private var ips = arrayOf("http://0.0.0.0:9080", "http://10.147.17.126:9080", "http://192.168.0.233:9080", "http://truenas.local:9080")
private var apiAddress = "";
private val minimum_api_version : Long = 3;

// TODO: add cache limits
val JSONCache = Hashtable<String, JsonObject>()
val imageCache = Hashtable<String, Bitmap>()

// launch inside a scope please, path without address like "/first/second" or a full address
suspend fun DownloadJSON(scope: LifecycleCoroutineScope, address: String, notifyText: TextView?): JsonObject {
    notifyText?.text = "Loading..."

    if (JSONCache.containsKey(address)) {
        val result = JSONCache.get(address)
        if (result != null) {
            return result;
        }
    }

    var attempts = 0;
    do {
        attempts++
        if (attempts > 1) notifyText?.text = "Failed! Attempt number: " + attempts.toString()

        try {
            val url = if (address.startsWith("/")) ApiAddress(scope) + address;
            else address

            val value = withContext(Dispatchers.IO) {
                URL(url).readText()
            }
            notifyText?.text = ""
            val json = Json{ignoreUnknownKeys = true}.parseToJsonElement(value).jsonObject
            JSONCache.put(address, json)
            return json
        }
        catch (_ : Exception) {
            apiAddress = ""
        }

    }
    while(attempts < 3)

    notifyText?.text = "Failed to download!"
    throw Exception("Failed to load content!")
}
// launch inside a scope please, path without address like "/first/second"
suspend fun DownloadBitmap(scope: LifecycleCoroutineScope, address: String, notifyText: TextView?): Bitmap? {
    notifyText?.text = "Loading..."

    if (imageCache.containsKey(address)) {
        val result = imageCache.get(address)
        if (result != null) {
            return result;
        }
    }

    var attempts = 0;
    do {
        attempts++
        if (attempts > 1) notifyText?.text = "Failed! Attempt number: " + attempts.toString()

        try {
            val url = if (address.startsWith("/")) ApiAddress(scope) + address;
            else address

            val bytes = withContext(Dispatchers.IO) {
                URL(url).readBytes()
            }
            notifyText?.text = ""
            val image = BitmapFactory.decodeByteArray(bytes, 0, bytes.size);
            imageCache.put(address, image)
            return image
        }
        catch (_ : Exception) {
            apiAddress = ""
        }

    }
    while(attempts < 3)

    notifyText?.text = "Failed to download!"
    throw Exception("Failed to load content!")
}

var mutex : Mutex = Mutex()

suspend fun ApiAddress(scope: LifecycleCoroutineScope): String {
    if (apiAddress !== "") return apiAddress

    val jobs = ArrayList<Job>()
    for (n in ips) {
        jobs.add(scope.launch {
            try {
                val address = n + "/"
                val result = withContext(Dispatchers.IO) {
                    withTimeout(2000) {
                        URL(address).readText()
                }}
                val json = Json {ignoreUnknownKeys = true}.parseToJsonElement(result).jsonObject;
                if (json["api_version"].toString().removeSurrounding("\"").toLong() >= minimum_api_version) {
                    mutex.lock()
                    if (apiAddress == "") apiAddress = n;
                    mutex.unlock()
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }

    for (n in jobs) {
        if (apiAddress != "") return apiAddress
        n.join()
    }

    return apiAddress;
}
