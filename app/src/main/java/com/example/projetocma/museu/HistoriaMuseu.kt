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
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentHistoriaMuseuBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale


class HistoriaMuseu : Fragment() {

    private var _binding: FragmentHistoriaMuseuBinding? = null
    private val binding get() = _binding!!
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val name = arguments?.getString("name")
        val imageResId = arguments?.getByteArray("image")
        val description = arguments?.getString("description")

        val navView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        navView.visibility = View.GONE

        val decodedBitmap = BitmapFactory.decodeByteArray(imageResId, 0, imageResId!!.size)

        imageResId?.let {
            binding.historiaImage.setImageBitmap(decodedBitmap)
        }

        showDialog(name, decodedBitmap, description)

        binding.historiaImage.setOnClickListener {
            showDialog(name, decodedBitmap, description)
        }

        binding.setaBackk.setOnClickListener {
            findNavController().popBackStack()
            if (::textToSpeech.isInitialized) {
                textToSpeech.stop()
                textToSpeech.shutdown()
            }
        }
    }

    private fun showDialog(name: String?,  imageBitmap: Bitmap?, description: String?) {
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

        imageBitmap?.let {
            bottomSheetImageView.setImageBitmap(imageBitmap)
        }
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
