package com.fz.microviewerapp.ui.main

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import com.fz.microviewerapp.ActivityDetails
import com.fz.microviewerapp.ApiAddress

import com.fz.microviewerapp.R
import com.fz.microviewerapp.databinding.ActivityActivityDetailsBinding
import com.fz.microviewerapp.databinding.FragmentMainBinding
import com.fz.microviewerapp.databinding.FragmentManufacturersBoardListBinding
import com.fz.microviewerapp.ui.manufacturers.ManufacturersBoardList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import java.net.URL

class ActivityDetailsFragment : Fragment() {

    private var boa_id: Long? = null

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    companion object {
        @JvmStatic
        fun newInstance(boa_id : Long) =
            ActivityDetailsFragment().apply {
                arguments = Bundle().apply {
                    putLong("boa_id", boa_id)
                }
            }
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            boa_id = it.getLong("boa_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val address = ApiAddress() + "details/" + boa_id.toString()
                val result = withContext(Dispatchers.IO) {
                    URL(address).readText()
                }
                // now let's parse the json
                val json = Json {ignoreUnknownKeys = true}.parseToJsonElement(result).jsonObject;
                try {
                    val link = json["boa_image"].toString().removeSurrounding("\"")
                    val bytes = withContext(Dispatchers.IO) {
                        URL(link).readBytes()
                    }
                    val image = BitmapFactory.decodeByteArray(bytes, 0, bytes.size);
                    binding.mainImage.setImageBitmap(image)
                }
                catch (e: Exception) {}

                //binding.manBoaLoadingText.text = ""
            } catch (e: Exception) {
                e.printStackTrace()
                //binding.manBoaLoadingText.textSize /= 2f;
                //binding.manBoaLoadingText.text = "Error:\n" +  e.message;
            }
        }
    }
}