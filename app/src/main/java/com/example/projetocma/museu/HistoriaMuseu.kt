package com.example.projetocma.museu

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentHistoriaMuseuBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class HistoriaMuseu : Fragment() {

    private lateinit var binding: FragmentHistoriaMuseuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoriaMuseuBinding.inflate(inflater, container, false)
        val name = arguments?.getString("name")
        val imageResId = arguments?.getByteArray("image")
        val description = arguments?.getString("description")

        val view = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        view.visibility = View.GONE

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
        }

        return binding.root
    }

    private fun showDialog(name: String?,  imageBitmap: Bitmap?, description: String?) {
        val dialog: Dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fragment_historia_bottom_sheet)


        val bottomSheetImageView: ImageView = dialog.findViewById(R.id.bottomSheetHistoriaImg)
        val bottomSheetName: TextView =  dialog.findViewById(R.id.textViewImgTitle)
        val bottomSheetDescription: TextView =  dialog.findViewById(R.id.textViewDescriptionHistoria)

        imageBitmap?.let {
            bottomSheetImageView.setImageBitmap(imageBitmap)
        }
        bottomSheetDescription.text = description
        bottomSheetName.text = name

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

}