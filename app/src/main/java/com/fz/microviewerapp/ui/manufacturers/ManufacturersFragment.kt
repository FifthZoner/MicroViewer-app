package com.fz.microviewerapp.ui.manufacturers

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fz.microviewerapp.databinding.FragmentManufacturersBinding
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import kotlin.coroutines.CoroutineContext
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonObject


class ManufacturersFragment : Fragment() {

    private var _binding: FragmentManufacturersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val manufacturersViewModel =
            ViewModelProvider(this).get(ManufacturersViewModel::class.java)

        _binding = FragmentManufacturersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    URL("http://192.168.0.233:9080/manufacturers").readText()
                }
                // now let's parse the json
                val json = Json {ignoreUnknownKeys = true}.parseToJsonElement(result).jsonObject;
                val array = json["manufacturers"] as JsonArray
                for (name in array) {
                    val button : Button = Button(context);
                    button.text = name.jsonObject["man_name"].toString().removeSurrounding("\"")
                    button.isAllCaps = false;
                    button.id = Integer.parseInt(name.jsonObject["man_id"].toString().removeSurrounding("\""))
                    button.setOnClickListener {
                        val k = button.id
                    }
                    (binding.manufacturersList.get(0) as LinearLayout).addView(button);
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}