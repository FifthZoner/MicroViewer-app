package com.fz.microviewerapp.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.core.view.children
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import com.fz.microviewerapp.ActivityDetails
import com.fz.microviewerapp.ApiAddress
import com.fz.microviewerapp.DownloadJSON
import com.fz.microviewerapp.R
import com.fz.microviewerapp.databinding.FragmentCategoryBoardsBinding
import com.fz.microviewerapp.databinding.FragmentSearchBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonObject
import java.net.URL

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        super.onViewCreated(view, savedInstanceState)

        binding.searchInput.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.length?.let {
                    if (it > 0) {
                        viewLifecycleOwner.lifecycleScope.launch {
                            try {
                                /*val address = ApiAddress() + "search/" + newText*/
                                /*val result = withContext(Dispatchers.IO) {*/
                                /*    URL(address).readText()*/
                                /*}*/
                                /*// now let's parse the json*/
                                /*val json = Json {ignoreUnknownKeys = true}.parseToJsonElement(result).jsonObject;*/
                                val json = DownloadJSON(viewLifecycleOwner.lifecycleScope, "/search/" + newText, null)
                                val array = json["boards"] as JsonArray
                                binding.searchList.removeAllViews()
                                for (name in array) {
                                    val button : Button = Button(context);
                                    button.text = name.jsonObject["boa_name"].toString().removeSurrounding("\"")
                                    button.isAllCaps = false;
                                    button.setOnClickListener {
                                        val intent = Intent(context, ActivityDetails::class.java)
                                        val boa_id = name.jsonObject["boa_id"].toString().removeSurrounding("\"").toLong()
                                        intent.putExtra("boa_id", boa_id)
                                        intent.putExtra("boa_name", name.jsonObject["boa_name"].toString().removeSurrounding("\""))
                                        startActivity(intent)
                                    }
                                    binding.searchList.addView(button)
                                }
                                //binding.catBoaLoadingText.text = ""
                            } catch (e: Exception) {
                                e.printStackTrace()
                                //binding.catBoaLoadingText.textSize /= 2f;
                                //binding.catBoaLoadingText.text = "Error:\n" +  e.message;
                            }
                        }
                    }
                    else binding.searchList.removeAllViews()
                }
                return false;
            }
        })

        binding.searchInput.isIconified = false
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SearchFragment().apply {
            }
    }
}