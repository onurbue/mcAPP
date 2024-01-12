package com.example.projetocma.account

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentTicketInfoBinding


class TicketInfo : Fragment() {
    private lateinit var binding: FragmentTicketInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTicketInfoBinding.inflate(inflater, container, false)

        val ticketId = arguments?.getString("ticketId")
        val price = arguments?.getString("price")
        val date = arguments?.getString("date")
        val description = arguments?.getString("description")
        val name = arguments?.getString("name")
        val image = arguments?.getByteArray("image")

        if (image != null) {
            val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
            binding.ticketImg.setImageBitmap(bitmap)
        } else {
            // Handle the case where imageByteArray is null (e.g., show a default image)
            binding.ticketImg.setImageResource(R.drawable.default_image)
        }

        binding.ticketName.text = name
        binding.ticketPrice.text = price + "€"
        binding.ticketdate.text = date
        binding.ticketdescription.text = description
        binding.ticketid.text = ticketId

        binding.setaBackk.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }


}