package com.example.projetocma.obras

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentObrasDetailBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import models.Obras
import models.Utility
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ObrasDetail : Fragment() {

    private  var _binding: FragmentObrasDetailBinding? = null
    private lateinit var textToSpeech: TextToSpeech
    var title: String? = null
    var pathToImage: String? = null
    var description: String? = null
    var imgDescription: String? = null
    var obraId: String? = null
    var museuId: String? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString("name")
        description = arguments?.getString("description")
        imgDescription = arguments?.getString("imgDescription")
        obraId = arguments?.getString("obraId")
        museuId = arguments?.getString("museuId")
        pathToImage = arguments?.getString("image")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        _binding = FragmentObrasDetailBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navBar =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        navBar.visibility = View.GONE

        if (museuId != null && obraId != null) {
            Obras.fetchObra(museuId, obraId) {
                val obra = Obras(
                    id = it.id,
                    name = it.name,
                    image = it.image,
                    description = it.description,
                    imgDescription = it.imgDescription

                )
                Log.d("Firestore", "Fetched image path: ${obra.image}")
                pathToImage = obra.image
                title = obra.name
                description = obra.description
                imgDescription = obra.imgDescription

                Utility.setImage(pathToImage, binding.obrasImg, requireContext())
                showDialog(imgDescription, description, title, pathToImage)

                binding.obrasImg.setOnClickListener {
                    showDialog(imgDescription, description, title, pathToImage)
                }

            }

            binding.setaBackk.setOnClickListener {
                findNavController().navigate(R.id.museusExplore)
                if (::textToSpeech.isInitialized) {
                    textToSpeech.stop()
                    textToSpeech.shutdown()
                }
            }
        }
        else{
            Utility.setImage(pathToImage, binding.obrasImg, requireContext())
            showDialog(imgDescription, description, title, pathToImage)

            binding.obrasImg.setOnClickListener {
                showDialog(imgDescription, description, title, pathToImage)
            }

            binding.setaBackk.setOnClickListener {
                findNavController().popBackStack()
                if (::textToSpeech.isInitialized) {
                    textToSpeech.stop()
                    textToSpeech.shutdown()
                }
            }
        }





    }

    private fun showDialog(
        imgDescription: String?,
        description: String?,
        name: String?,
        pathToImage: String?
    ) {
        val dialog: Dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fragment_obras_bottom_sheet)

        val audioButton: ImageView = dialog.findViewById(R.id.audiobutton)
        val bottomSheetImageView: ImageView = dialog.findViewById(R.id.bottomSheetObrasImg)
        val bottomSheetImgDescription: TextView = dialog.findViewById(R.id.textViewImgObrasDescription)
        val bottomSheetDescription: TextView = dialog.findViewById(R.id.textViewObrasDescription)
        val bottomSheetName: TextView = dialog.findViewById(R.id.textViewImgObrasName)

        textToSpeech = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale.getDefault())
                if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                    Toast.makeText(requireContext(), "language is not supported", Toast.LENGTH_SHORT).show()
                }
            }
        }

            Utility.setImage(pathToImage, bottomSheetImageView, requireContext())

        bottomSheetDescription.text = description
        bottomSheetImgDescription.text = imgDescription
        bottomSheetName.text = name

        audioButton.setOnClickListener {
            if (description!!.isNotEmpty()) {
                textToSpeech.speak(description, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }

        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }








}