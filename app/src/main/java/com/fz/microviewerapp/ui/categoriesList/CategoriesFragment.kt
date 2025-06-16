package com.fz.microviewerapp.ui.categoriesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fz.microviewerapp.databinding.FragmentCategoriesBinding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonObject
import android.widget.Button
import android.content.Intent
import android.widget.LinearLayout
import androidx.core.view.get
import com.fz.microviewerapp.connectivity.DownloadJSON
import com.fz.microviewerapp.ui.categoryBoards.CategoryBoards
import kotlin.jvm.java

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val categoriesViewModel =
            ViewModelProvider(this).get(CategoriesViewModel::class.java)

        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                /*val result = withContext(Dispatchers.IO) {
                    URL(ApiAddress() + "categories").readText()
                }
                // now let's parse the json
                val json = Json {ignoreUnknownKeys = true}.parseToJsonElement(result).jsonObject;*/
                val json = DownloadJSON(viewLifecycleOwner.lifecycleScope, "/categories", binding.loadingText)
                val array = json["categories"] as JsonArray
                for (name in array) {
                    val button : Button = Button(context);
                    button.text = name.jsonObject["cat_name"].toString().removeSurrounding("\"")
                    button.isAllCaps = false;
                    button.setOnClickListener {
                        val intent = Intent(context, CategoryBoards::class.java)
                        val cat_id = name.jsonObject["cat_id"].toString().removeSurrounding("\"").toLong()
                        intent.putExtra("cat_id", cat_id)
                        intent.putExtra("cat_name", name.jsonObject["cat_name"].toString().removeSurrounding("\""))
                        startActivity(intent)
                    }
                    (binding.categoriesList.get(0) as LinearLayout).addView(button);
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