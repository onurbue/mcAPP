package com.example.projetocma.tickets

import android.content.ContentValues.TAG
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.CompoundButtonCompat
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentTicketBasiccBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import models.Tickets
import models.TicketsComprados
import models.Utility
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


class TicketBasic : Fragment() {
    private lateinit var binding: FragmentTicketBasiccBinding

    var userId = Firebase.auth.currentUser?.uid
    var selectedDate: Date? = null
    var name : String? = null
    var description : String? = null
    var price : String? = null
    var pathToImage : String? = null
    var museuId : String? = null
    var ticketId : String? = null
    var formattedPrice: String? = null
    var updatedPrice: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         name = arguments?.getString("name")
         description = arguments?.getString("description")
         pathToImage = arguments?.getString("image")
         price = arguments?.getString("price")
         pathToImage = arguments?.getString("pathToImage")
         selectedDate = arguments?.getSerializable("selectedDate") as? Date
         museuId = arguments?.getString("museuId")
         ticketId = arguments?.getString("ticketId")
         formattedPrice = "$price €"



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val buttonTint = ContextCompat.getColor(requireContext(), R.color.black)
        binding = FragmentTicketBasiccBinding.inflate(inflater, container, false)
        CompoundButtonCompat.setButtonTintList(binding.checkbox, ColorStateList.valueOf(buttonTint))
        var quantity = 1

        val initialPrice = price!!.toInt()


        binding.ticketPrice.text = formattedPrice
        binding.ticketName.text = name
        binding.description.text = description
        binding.quantidade.text = quantity.toString()

        binding.addVector.setOnClickListener {
            if (quantity < 4){
                quantity++
            }
            binding.quantidade.text = quantity.toString()
             updatedPrice = initialPrice * quantity
            binding.ticketNamesPrice.text = "$updatedPrice €"
        }


        binding.minusVector.setOnClickListener {
            if (quantity > 1){
                quantity--
            }
            binding.quantidade.text = quantity.toString()
             updatedPrice = initialPrice * quantity
            binding.ticketNamesPrice.text = "$updatedPrice €"
        }

        Utility.setImage(pathToImage, binding.ticketImg, requireContext())

            val formattedDate = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(selectedDate)
            binding.data.text = formattedDate
            binding.ticketNamesPrice.text = formattedPrice



        binding.buttonNextBasic.setOnClickListener {
            val bundle = Bundle().apply {
                putString("formattedDate", formattedDate)
                putString("userId", userId)
                putString("name", name)
                putString("pathToImage", pathToImage)
                putString("description", description)
                putInt("price", updatedPrice!!)
                putString("museuId", museuId)
                putString("ticketId", ticketId)
                putInt("quantity", quantity)

            }


            findNavController().navigate(R.id.action_ticketBasicc_to_MBWay3, bundle)

        }

        binding.buttonBackBasic.setOnClickListener {
            findNavController().popBackStack()
        }


        return binding.root
    }



}