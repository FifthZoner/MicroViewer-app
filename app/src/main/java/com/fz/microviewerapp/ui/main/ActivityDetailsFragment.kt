package com.fz.microviewerapp.ui.main

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.fz.microviewerapp.ApiAddress
import com.fz.microviewerapp.databinding.FragmentMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import java.net.URL
import androidx.core.net.toUri


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
                    binding.chipText.text = json["chi_name"].toString().removeSurrounding("\"");
                    binding.manufacturerText.text = json["man_name"].toString().removeSurrounding("\"");
                    binding.categoryText.text = json["cat_name"].toString().removeSurrounding("\"");
                    binding.nameText.text = json["boa_name"].toString().removeSurrounding("\"");

                    val pin_link = json["boa_pin"].toString().removeSurrounding("\"");
                    if (pin_link != "" && pin_link != "null") {
                        val button : Button = Button(context);
                        button.text = "Pinout"
                        button.isAllCaps = false;
                        button.setOnClickListener {
                            startActivity(Intent(Intent.ACTION_VIEW, pin_link.toUri()))
                        }
                        binding.items.addView(button)
                    }
                    val sch_link = json["boa_sch"].toString().removeSurrounding("\"");
                    if (sch_link != "" && sch_link != "null") {
                        val button : Button = Button(context);
                        button.text = "Schematics"
                        button.isAllCaps = false;
                        button.setOnClickListener {
                            startActivity(Intent(Intent.ACTION_VIEW, sch_link.toUri()))
                        }
                        binding.items.addView(button)
                    }
                    val doc_link = json["boa_doc"].toString().removeSurrounding("\"");
                    if (doc_link != "" && doc_link != "null") {
                        val button : Button = Button(context);
                        button.text = "Documentation"
                        button.isAllCaps = false;
                        button.setOnClickListener {
                            startActivity(Intent(Intent.ACTION_VIEW, doc_link.toUri()))
                        }
                        binding.items.addView(button)
                    }
                    val size_link = json["boa_size"].toString().removeSurrounding("\"");
                    if (size_link != "" && size_link != "null") {
                        val button : Button = Button(context);
                        button.text = "Size"
                        button.isAllCaps = false;
                        button.setOnClickListener {
                            startActivity(Intent(Intent.ACTION_VIEW, size_link.toUri()))
                        }
                        binding.items.addView(button)
                    }

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