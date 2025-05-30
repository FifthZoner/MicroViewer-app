package com.fz.microviewerapp;

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import java.net.URL;

private var ips = arrayOf("http://0.0.0.0:9080/", "http://192.168.0.233:9080/", "http://10.147.17.126:9080/")
private var address = "";
private val minimum_api_version : Long = 3;

fun FindAddress() {
    for (add in ips) {
        try {
            val result = URL(add).readText();
            val json = Json {ignoreUnknownKeys = true}.parseToJsonElement(result).jsonObject;
            if (json["api_version"].toString().removeSurrounding("\"").toLong() < minimum_api_version) continue
            address = add;
            return;
        }
        catch (e: Exception) {
            continue;
        }
    }
    throw Exception("No viable server found!");
}

fun ApiAddress(): String {
    if (address == "") FindAddress();
    return address;
}
