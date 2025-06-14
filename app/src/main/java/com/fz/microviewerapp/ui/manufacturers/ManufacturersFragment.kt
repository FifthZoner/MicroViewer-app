package com.fz.microviewerapp.ui.manufacturers

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.Button
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fz.microviewerapp.databinding.FragmentManufacturersBinding
import androidx.lifecycle.lifecycleScope
import com.fz.microviewerapp.ApiAddress
import com.fz.microviewerapp.DownloadJSON
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
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
                /*val result = withContext(Dispatchers.IO) {
                    URL(ApiAddress() + "manufacturers").readText()
                }
                // now let's parse the json
                val json = Json {ignoreUnknownKeys = true}.parseToJsonElement(result).jsonObject;*/
                val json = DownloadJSON(viewLifecycleOwner.lifecycleScope, "/manufacturers", binding.loadingText)
                val array = json["manufacturers"] as JsonArray
                for (name in array) {
                    val button : Button = Button(context);
                    button.text = name.jsonObject["man_name"].toString().removeSurrounding("\"")
                    button.isAllCaps = false;
                    button.setOnClickListener {
                        val intent = Intent(context, ManufacturerBoards::class.java)
                        val man_id = name.jsonObject["man_id"].toString().removeSurrounding("\"").toLong()
                        intent.putExtra("man_id", man_id)
                        intent.putExtra("man_name", name.jsonObject["man_name"].toString().removeSurrounding("\""))
                        startActivity(intent)
                    }
                    (binding.manufacturersList.get(0) as LinearLayout).addView(button);
                }
            } catch (e: Exception) {
                e.printStackTrace()
                binding.loadingText.textSize /= 2f;
                binding.loadingText.text = "Error:\n" +  e.message;
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}