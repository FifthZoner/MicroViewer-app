package com.fz.microviewerapp;

import java.net.URL;

private var ips = arrayOf("http://10.147.17.126:9080/", "http://192.168.0.233:9080/")
private var address = "";

fun FindAddress() {
    for (add in ips) {
        try {
            val result = URL(add).readText();
            address = add;
            return;
        }
        catch (e: Exception) {
            continue;
        }
    }
    throw Exception("No viable address!");
}

fun ApiAddress(): String {
    if (address == "") FindAddress();
    return address;
}
