package com.example.projetocma.eventos

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
import com.example.projetocma.databinding.FragmentEventDetailBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import models.Utility
import java.util.Locale


class EventDetail : Fragment() {
    var pathToImage: String? = null
    var description: String? = null
    var imgDescription: String? = null

    private  var _binding: FragmentEventDetailBinding? = null
    private lateinit var textToSpeech: TextToSpeech
    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pathToImage = arguments?.getString("image")
        description = arguments?.getString("description")
        imgDescription = arguments?.getString("imgDescription")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        view.visibility = View.GONE

        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)


        Utility.setImage(pathToImage, binding.tecxt123, requireContext())

            showDialog(imgDescription, pathToImage, description)

        binding.setaBackk.setOnClickListener {
            findNavController().popBackStack()
            if (::textToSpeech.isInitialized) {
                textToSpeech.stop()
                textToSpeech.shutdown()
            }
        }


        return binding.root
    }

    private fun showDialog(imgDescription: String?,  imagePath: String?, description: String?) {
        val dialog: Dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fragment_event_bottom_sheet)

        val audioButton : ImageView = dialog.findViewById(R.id.audiobutton)
        val bottomSheetImageView: ImageView = dialog.findViewById(R.id.bottomSheetImg)
        val bottomSheetImgDescription: TextView =  dialog.findViewById(R.id.textViewImgDescription)
        val bottomSheetDescription: TextView =  dialog.findViewById(R.id.textViewDescription)

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
        bottomSheetImgDescription.text = imgDescription

        audioButton.setOnClickListener {
            if (description!!.isNotEmpty()){
                textToSpeech.speak(description,TextToSpeech.QUEUE_FLUSH,null,null)
            }
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }



}