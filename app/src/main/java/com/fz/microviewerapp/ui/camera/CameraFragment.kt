package com.fz.microviewerapp.ui.camera

import android.Manifest
import android.R.string
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.fz.microviewerapp.R
import com.fz.microviewerapp.databinding.FragmentCameraBinding
import com.fz.microviewerapp.databinding.FragmentMainBinding
import com.google.common.util.concurrent.ListenableFuture
import kotlin.random.Random
import kotlin.text.String
import kotlin.text.get


class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>

    override fun onCreate(savedInstanceState: Bundle?) {

        if (context != null) {
            val con : Context = requireContext()
            val act : Activity = requireActivity()

            if (ContextCompat.checkSelfPermission(con, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(act, arrayOf(Manifest.permission.CAMERA), 101)
            }
            else {
                cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
                cameraProviderFuture.addListener(Runnable {
                    val cameraProvider = cameraProviderFuture.get()
                    bindPreview(cameraProvider)
                }, ContextCompat.getMainExecutor(requireContext()))
            }
        }

        super.onCreate(savedInstanceState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
                cameraProviderFuture.addListener(Runnable {
                    val cameraProvider = cameraProviderFuture.get()
                    bindPreview(cameraProvider)
                }, ContextCompat.getMainExecutor(requireContext()))
            }
        }
    }

    fun bindPreview(cameraProvider : ProcessCameraProvider) {
        var preview : Preview = Preview.Builder()
            .build()

        var cameraSelector : CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(binding.cameraView.getSurfaceProvider())

        var camera = cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)
        binding.cameraView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        binding.cameraView.scaleType = PreviewView.ScaleType.FIT_CENTER
        binding.cameraOverlay.setImageResource(R.drawable.ebb36v1overlay)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            CameraFragment().apply {
            }
    }
}