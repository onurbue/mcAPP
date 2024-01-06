package com.example.projetocma.obras

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentObrasDetailBinding

class ObrasDetail : Fragment() {

    private lateinit var binding: FragmentObrasDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentObrasDetailBinding.inflate(inflater, container, false)

        val title = arguments?.getString("name")
        val imageResId = arguments?.getByteArray("image")
        val description = arguments?.getString("description")
        val imgDescription = arguments?.getString("imgDescription")

        val decodedBitmap = BitmapFactory.decodeByteArray(imageResId, 0, imageResId!!.size)

        imageResId?.let {
            binding.obrasImg.setImageBitmap(decodedBitmap)
        }

        showDialog(imgDescription, decodedBitmap, description, title)

        binding.obrasImg.setOnClickListener {
            showDialog(imgDescription, decodedBitmap, description, title)
        }

        binding.setaBackk.setOnClickListener {
            findNavController().popBackStack()
        }


        return binding.root
    }

    private fun showDialog(imgDescription: String?, imageBitmap: Bitmap?, description: String?, name: String?) {
        val dialog: Dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fragment_obras_bottom_sheet)


        val bottomSheetImageView: ImageView = dialog.findViewById(R.id.bottomSheetObrasImg)
        val bottomSheetImgDescription: TextView =  dialog.findViewById(R.id.textViewImgObrasDescription)
        val bottomSheetDescription: TextView =  dialog.findViewById(R.id.textViewObrasDescription)
        val bottomSheetName : TextView = dialog.findViewById(R.id.textViewImgObrasName)

        imageBitmap?.let {
            bottomSheetImageView.setImageBitmap(it)
        }
        bottomSheetDescription.text = description
        bottomSheetImgDescription.text = imgDescription
        bottomSheetName.text = name

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