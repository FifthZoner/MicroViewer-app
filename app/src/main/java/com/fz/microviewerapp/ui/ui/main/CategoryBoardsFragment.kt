package com.fz.microviewerapp.ui.ui.main

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.view.View.GONE
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import com.fz.microviewerapp.ActivityDetails
import com.fz.microviewerapp.ApiAddress
import com.fz.microviewerapp.R
import com.fz.microviewerapp.databinding.ActivityManufacturerBoardsBinding
import com.fz.microviewerapp.databinding.FragmentCategoryBoardsBinding
import com.fz.microviewerapp.databinding.FragmentManufacturersBinding
import com.fz.microviewerapp.databinding.FragmentManufacturersBoardListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonObject
import java.net.URL


class CategoryBoardsFragment : Fragment() {

    private var cat_id: Long? = null

    private var _binding: FragmentCategoryBoardsBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(cat_id : Long) = CategoryBoardsFragment().apply {
            arguments = Bundle().apply {
                putLong("cat_id", cat_id)
            }
        }
    }

    private val viewModel: CategoryBoardsModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cat_id = it.getLong("cat_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBoardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val address = ApiAddress() + "category/" + cat_id.toString()
                val result = withContext(Dispatchers.IO) {
                    URL(address).readText()
                }
                // now let's parse the json
                val json = Json {ignoreUnknownKeys = true}.parseToJsonElement(result).jsonObject;
                val array = json["boards"] as JsonArray
                for (name in array) {
                    val button : Button = Button(context);
                    button.text = name.jsonObject["boa_name"].toString().removeSurrounding("\"")
                    button.isAllCaps = false;
                    button.setOnClickListener {
                        val intent = Intent(context, ActivityDetails::class.java)
                        val boa_id = name.jsonObject["boa_id"].toString().removeSurrounding("\"").toLong()
                        intent.putExtra("boa_id", boa_id)
                        intent.putExtra("boa_name", name.jsonObject["boa_name"].toString().removeSurrounding("\""))
                        startActivity(intent)}
                    (binding.boardsList.get(0) as LinearLayout).addView(button);
                }
                binding.catBoaLoadingText.text = ""
            } catch (e: Exception) {
                e.printStackTrace()
                binding.catBoaLoadingText.textSize /= 2f;
                binding.catBoaLoadingText.text = "Error:\n" +  e.message;
            }
        }
    }

}