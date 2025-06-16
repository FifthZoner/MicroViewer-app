package com.fz.microviewerapp.ui.details

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.fz.microviewerapp.connectivity.DownloadBitmap
import com.fz.microviewerapp.connectivity.DownloadJSON
import com.fz.microviewerapp.databinding.FragmentMainBinding
import com.fz.microviewerapp.ui.camera.CameraActivity
import com.fz.microviewerapp.ui.main.MainViewModel
import kotlinx.coroutines.launch

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

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.scrollview.setOnApplyWindowInsetsListener { v, insets ->
            val navigationBarHeight = insets.getInsets(WindowInsets.Type.systemBars()).bottom
            v.setPadding(0, 0, 0, navigationBarHeight)
            insets
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val json = DownloadJSON(
                    viewLifecycleOwner.lifecycleScope,
                    "/details/" + boa_id.toString(),
                    null
                )
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

                    val boa_overlay_oid = json["boa_overlay_oid"].toString().removeSurrounding("\"");
                    if (boa_overlay_oid != "0" && boa_overlay_oid != "null") {
                        val button : Button = Button(context);
                        button.text = "Camera overlay"
                        button.isAllCaps = false;
                        button.setOnClickListener {
                            val intent = Intent(context, CameraActivity::class.java)
                            intent.putExtra("boa_overlay_oid", boa_overlay_oid.toLong())
                            startActivity(intent)
                        }
                        binding.items.addView(button)
                    }

                    val boardImage = DownloadBitmap(
                        viewLifecycleOwner.lifecycleScope,
                        json["boa_image"].toString().removeSurrounding("\""),
                        null
                    )
                    binding.mainImage.setImageBitmap(boardImage)
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