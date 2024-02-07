package com.example.projetocma.museu

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentHistoriaMuseuBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mapbox.maps.extension.style.expressions.dsl.generated.image
import models.Utility
import java.util.Locale


class HistoriaMuseu : Fragment() {

    private var _binding: FragmentHistoriaMuseuBinding? = null
    private val binding get() = _binding!!
    private lateinit var textToSpeech: TextToSpeech

    private var name: String? = null
    private var imagePath: String? = null
    private var description: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        name = arguments?.getString("name")
        imagePath = arguments?.getString("image")
        description = arguments?.getString("description")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoriaMuseuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        navView.visibility = View.GONE

        Utility.setImage(imagePath, binding.historiaImage, requireContext())

        showDialog(name, imagePath, description)

        binding.historiaImage.setOnClickListener {
            showDialog(name, imagePath, description)
        }

        binding.setaBackk.setOnClickListener {
            findNavController().popBackStack()
            if (::textToSpeech.isInitialized) {
                textToSpeech.stop()
                textToSpeech.shutdown()
            }
        }
    }

    private fun showDialog(name: String?,  imagePath: String?, description: String?) {
        val dialog: Dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fragment_historia_bottom_sheet)

        val audioButton: ImageView = dialog.findViewById(R.id.audiobutton)
        val bottomSheetImageView: ImageView = dialog.findViewById(R.id.bottomSheetHistoriaImg)
        val bottomSheetName: TextView = dialog.findViewById(R.id.textViewImgTitle)
        val bottomSheetDescription: TextView = dialog.findViewById(R.id.textViewDescriptionHistoria)

        textToSpeech = TextToSpeech(requireContext()){status ->
            if(status == TextToSpeech.SUCCESS){
                val result = textToSpeech.setLanguage(Locale.getDefault())
                if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA){
                    Toast.makeText(requireContext(),"language is not supported", Toast.LENGTH_SHORT).show()
                }
            }
        }

        Utility.setImage(imagePath, bottomSheetImageView, requireContext())
        bottomSheetDescription.text = description
        bottomSheetName.text = name

        audioButton.setOnClickListener {
            if (description!!.isNotEmpty()){
                textToSpeech.speak(description, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Set the binding to null to release resources
    }
}
