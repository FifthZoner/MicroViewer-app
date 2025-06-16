package com.fz.microviewerapp.ui.categoryBoards

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.fz.microviewerapp.ui.details.ActivityDetails
import com.fz.microviewerapp.connectivity.DownloadJSON
import com.fz.microviewerapp.databinding.FragmentCategoryBoardsBinding
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonObject

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

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.categoryBoards.setOnApplyWindowInsetsListener { v, insets ->
            val navigationBarHeight = insets.getInsets(WindowInsets.Type.systemBars()).bottom
            v.setPadding(0, 0, 0, navigationBarHeight)
            insets
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                /*val address = ApiAddress() + "category/" + cat_id.toString()*/
                /*val result = withContext(Dispatchers.IO) {*/
                /*    URL(address).readText()*/
                /*}*/
                /*// now let's parse the json*/
                /*val json = Json {ignoreUnknownKeys = true}.parseToJsonElement(result).jsonObject;*/
                val json = DownloadJSON(
                    viewLifecycleOwner.lifecycleScope,
                    "/category/" + cat_id.toString(),
                    binding.catBoaLoadingText
                )
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
            } catch (e: Exception) {
                e.printStackTrace()
                binding.catBoaLoadingText.textSize /= 2f;
                binding.catBoaLoadingText.text = "Error:\n" +  e.message;
            }
        }
    }

}