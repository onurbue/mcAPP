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
import java.util.Locale


class EventDetail : Fragment() {

    private lateinit var binding: FragmentEventDetailBinding
    private lateinit var textToSpeech: TextToSpeech
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        view.visibility = View.GONE

        binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        val imageResId = arguments?.getByteArray("image")
        val description = arguments?.getString("description")
        val imgDescription = arguments?.getString("imgDescription")

        val decodedBitmap = BitmapFactory.decodeByteArray(imageResId, 0, imageResId!!.size)

        imageResId?.let {
            binding.tecxt123.setImageBitmap(decodedBitmap)
        }



            showDialog(imgDescription, decodedBitmap, description)

        binding.setaBackk.setOnClickListener {
            findNavController().popBackStack()
            if (::textToSpeech.isInitialized) {
                textToSpeech.stop()
                textToSpeech.shutdown()
            }
        }


        return binding.root
    }

    private fun showDialog(imgDescription: String?,  imageBitmap: Bitmap?, description: String?) {
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

        imageBitmap?.let {
            bottomSheetImageView.setImageBitmap(imageBitmap)
        }
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