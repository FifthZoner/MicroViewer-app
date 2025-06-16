package com.fz.microviewerapp

import android.graphics.BitmapFactory
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fz.microviewerapp.connectivity.DownloadJSON
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * A [Rule] that creates a [LifecycleOwner] with a [LifecycleRegistry] that can be used in tests.
 * The lifecycle is initially in the [Lifecycle.State.CREATED] state and is set to [Lifecycle.State.DESTROYED] when the test finishes.
 * You can use [lifecycleRegistry]'s [LifecycleRegistry.currentState] setter to change the state of the lifecycle.
 */
class LifecycleOwnerRule : TestWatcher() {

    val lifecycleOwner: LifecycleOwner = object : LifecycleOwner {
        override val lifecycle: Lifecycle get() = lifecycleRegistry
    }

    val lifecycleRegistry by lazy { LifecycleRegistry(lifecycleOwner) }

    override fun starting(description: Description?) {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    override fun finished(description: Description?) {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }
}

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.fz.microviewerapp", appContext.packageName)
    }

    suspend fun fetchJson(address: String): JsonObject? {
        return try {
            val result = DownloadJSON(LifecycleOwnerRule().lifecycleOwner.lifecycleScope, address, null)
            result
        } catch (_: Exception) {
            null
        }
        assert(false)
    }

    @Test
    fun apiConnection() = runBlocking {
        val json = fetchJson("/")
        assertNotNull(json)
    }

    @Test
    fun categoryBrowser() = runBlocking {
        val json = fetchJson("/categories")
        try {
            if (json != null) {
                val array = json["categories"] as JsonArray
                for (item in array) {
                    assertTrue((item as JsonObject).containsKey("cat_name")
                            && item["cat_name"].toString().removeSurrounding("\"").isNotEmpty()
                            && item.containsKey("cat_id")
                            && item["cat_id"].toString().removeSurrounding("\"").toLong() > 0)
                    return@runBlocking
                }
            }
        }
        catch (_: Exception) {}
        assert(false)
    }

    @Test
    fun manufacturerBrowser() = runBlocking {
        val json = fetchJson("/manufacturers")
        try {
            if (json != null) {
                val array = json["manufacturers"] as JsonArray
                for (item in array) {
                    assertTrue((item as JsonObject).containsKey("man_name")
                            && item["man_name"].toString().removeSurrounding("\"").isNotEmpty()
                            && item.containsKey("man_id")
                            && item["man_id"].toString().removeSurrounding("\"").toLong() > 0)
                    return@runBlocking
                }
            }
        }
        catch (_: Exception) {}
        assert(false)
    }

    @Test
    fun boardSearch1() = runBlocking {
        val json = fetchJson("/search/r a s B p e Rr y   p I 5 rasp   bERry   rasPBerry")
        try {
            if (json != null) {
                val array = json["boards"] as JsonArray
                for (item in array) {
                    assertTrue((item as JsonObject).containsKey("boa_name")
                            && item["boa_name"].toString().removeSurrounding("\"").isNotEmpty()
                            && item.containsKey("boa_id")
                            && item["boa_id"].toString().removeSurrounding("\"").toLong() > 0)
                    return@runBlocking
                }
            }
        }
        catch (_: Exception) {}
        assert(false)
    }

    @Test
    fun boardSearch2() = runBlocking {
        val json = fetchJson("/search/voRoN Levia th an viathan")
        try {
            if (json != null) {
                val array = json["boards"] as JsonArray
                for (item in array) {
                    assertTrue((item as JsonObject).containsKey("boa_name")
                            && item["boa_name"].toString().removeSurrounding("\"").isNotEmpty()
                            && item.containsKey("boa_id")
                            && item["boa_id"].toString().removeSurrounding("\"").toLong() > 0)
                    return@runBlocking
                }
            }
        }
        catch (_: Exception) {}
        assert(false)
    }

    @Test
    fun boardSearch3() = runBlocking {
        val json = fetchJson("/search/pdakfjadlfknloajidnasdfvg lgfsjnk lsdg dfkjg fgbz kzbjd jkbfh")
        try {
            if (json != null) {
                val array = json["boards"] as JsonArray
                if (array.isNotEmpty()) assert(false)
            }
        }
        catch (_: Exception) {}
    }

    @Test
    fun boardSearch4() = runBlocking {
        val json = fetchJson("/search/@$^RYB%#&B&%B$&%B&TYR BDNSHG  HTE%Y$ UB^&UB^&$#BU%^")
        try {
            if (json != null) {
                val array = json["boards"] as JsonArray
                if (array.isNotEmpty()) assert(false)
            }
        }
        catch (_: Exception) {}
    }

    @Test
    fun manufacturerBoardsBrowser() = runBlocking {
        val json = fetchJson("/manufacturers")
        try {
            if (json != null) {
                val array = json["manufacturers"] as JsonArray
                for (item in array) {
                    val json2 = fetchJson("/manufacturer/" + (item as JsonObject)["man_id"].toString().removeSurrounding("\""))
                    if (json2 != null) {
                        val array2 = json2["boards"] as JsonArray
                        for (item2 in array2) {
                            assertTrue((item2 as JsonObject).containsKey("boa_name")
                                    && item2["boa_name"].toString().removeSurrounding("\"").isNotEmpty()
                                    && item2.containsKey("boa_id")
                                    && item2["boa_id"].toString().removeSurrounding("\"").toLong() > 0)
                            return@runBlocking
                        }
                    }
                }
            }
        }
        catch (_: Exception) {}
        assert(false)
    }

    @Test
    fun categoryBoardsBrowser() = runBlocking {
        val json = fetchJson("/categories")
        try {
            if (json != null) {
                val array = json["categories"] as JsonArray
                for (item in array) {
                    val json2 = fetchJson("/category/" + (item as JsonObject)["cat_id"].toString().removeSurrounding("\""))
                    if (json2 != null) {
                        val array2 = json2["boards"] as JsonArray
                        for (item2 in array2) {
                            assertTrue((item2 as JsonObject).containsKey("boa_name")
                                    && item2["boa_name"].toString().removeSurrounding("\"").isNotEmpty()
                                    && item2.containsKey("boa_id")
                                    && item2["boa_id"].toString().removeSurrounding("\"").toLong() > 0)
                            return@runBlocking
                        }
                    }
                }
            }
        }
        catch (_: Exception) {}
        assert(false)
    }

    @Test
    fun boardDetails() = runBlocking {
        val json = fetchJson("/search/e")
        try {
            if (json != null) {
                val array = json["boards"] as JsonArray
                for (item in array) {
                    val json2 = fetchJson("/details/" + (item as JsonObject)["boa_id"].toString().removeSurrounding("\""))
                    if (json2 != null) {
                        assertTrue(json2.containsKey("boa_name")
                                && json2["boa_name"].toString().removeSurrounding("\"").isNotEmpty()
                                && json2.containsKey("boa_image")
                                && json2["boa_image"].toString().removeSurrounding("\"").isNotEmpty()
                                && json2.containsKey("man_name")
                                && json2["man_name"].toString().removeSurrounding("\"").isNotEmpty()
                                && json2.containsKey("cat_name")
                                && json2["cat_name"].toString().removeSurrounding("\"").isNotEmpty()
                                && json2.containsKey("chi_name")
                                && json2["chi_name"].toString().removeSurrounding("\"").isNotEmpty()
                                && json2.containsKey("boa_doc")
                                && json2.containsKey("boa_sch")
                                && json2.containsKey("boa_pin")
                                && json2.containsKey("boa_overlay_oid")

                        )
                        return@runBlocking
                    }
                }
            }
        }
        catch (_: Exception) {}
        assert(false)
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun imageFetch() = runBlocking {
        val json = fetchJson("/image/1")
        try {
            if (json != null) {
                assertTrue(json.containsKey("image")
                        && json["image"].toString().removeSurrounding("\"").isNotEmpty()
                )
                val bytes = Base64.decode(json["image"].toString().removeSurrounding("\""))
                val boardImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                assertNotNull(boardImage)
                return@runBlocking
            }
        }
        catch (_: Exception) {}
        assert(false)
    }

}