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
import models.Utility


class TicketInfo : Fragment() {
    private  var _binding: FragmentTicketInfoBinding? = null
    var ticketId : String? = null
    var price : String? = null
    var date : String? = null
    var description : String? = null
    var name : String? = null
    var image : String? = null

    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         ticketId = arguments?.getString("ticketId")
         price = arguments?.getString("price")
         date = arguments?.getString("date")
         description = arguments?.getString("description")
         name = arguments?.getString("name")
         image = arguments?.getString("image")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTicketInfoBinding.inflate(inflater, container, false)

        Utility.setImage(image, binding.ticketImg, requireContext())
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