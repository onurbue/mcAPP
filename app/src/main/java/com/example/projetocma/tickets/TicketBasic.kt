package com.example.projetocma.tickets

import android.content.ContentValues.TAG
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentTicketBasiccBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import models.TicketsComprados
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


class TicketBasic : Fragment() {
    private lateinit var binding: FragmentTicketBasiccBinding
    val randomUid: String = UUID.randomUUID().toString()
    var userId = Firebase.auth.currentUser?.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var quantity = 1
        binding = FragmentTicketBasiccBinding.inflate(inflater, container, false)

        val name = arguments?.getString("name")
        val description = arguments?.getString("description")
        val image = arguments?.getByteArray("image")
        val price = arguments?.getString("price")
        val pathToImage = arguments?.getString("pathToImage")
        val selectedDate: Date? = arguments?.getSerializable("selectedDate") as? Date

        val initialPrice = price!!.toInt()

        binding.ticketPrice.text = price + "€"
        binding.ticketName.text = name
        binding.description.text = description
        binding.quantidade.text = quantity.toString()

        binding.addVector.setOnClickListener {
            quantity++
            binding.quantidade.text = quantity.toString()
            val updatedPrice = initialPrice * quantity
            binding.ticketNamesPrice.text = updatedPrice.toString() + "€"
        }

        binding.minusVector.setOnClickListener {
            if (quantity > 1){
                quantity--
            }
            binding.quantidade.text = quantity.toString()
            val updatedPrice = initialPrice * quantity
            binding.ticketNamesPrice.text = updatedPrice.toString() + "€"
        }

        if (image != null) {
            val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
            binding.ticketImg.setImageBitmap(bitmap)
        } else {
            // Handle the case where imageByteArray is null (e.g., show a default image)
            binding.ticketImg.setImageResource(R.drawable.default_image)
        }

            val formattedDate = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(selectedDate)
            binding.data.text = formattedDate
            binding.ticketNamesPrice.text = price

        binding.metodopagamento.setOnClickListener {
            showCategoryMenu(binding.metodopagamento)
        }


        val db = Firebase.firestore

        binding.buttonNextBasic.setOnClickListener {

            val db = FirebaseFirestore.getInstance()
             for (i in 0 until quantity) {

                 val ticket = TicketsComprados(
                     id = randomUid,
                     date = formattedDate,
                     userId = userId,
                     name = name,
                     pathToImg = pathToImage,
                     description = description,
                     price = price
                 )

                 val ticketMap = ticket.toHashMap()

                db.collection("bilhetesUser")
                    .add(ticketMap)
                    .addOnSuccessListener { documentReference ->
                        val ticketId = documentReference.id
                    }
                    .addOnFailureListener { e ->
                    }
            }

            showToast("O pagamento foi bem sucedido")
            findNavController().navigate(R.id.museusExplore)

        }

        binding.buttonBackBasic.setOnClickListener {
            findNavController().popBackStack()
        }


        return binding.root
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showCategoryMenu(anchorView: View) {
        val popupMenu = PopupMenu(requireContext(), anchorView)
        popupMenu.menuInflater.inflate(R.menu.metodo_pagamento, popupMenu.menu)

        // Set a click listener for each menu item
        /*popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_category_art -> filterMuseumsByCategory("Arte")
                R.id.menu_category_history -> filterMuseumsByCategory("Cultura")
                R.id.menu_category_none -> fetchMuseums()
                // Add more categories as needed
            }
            true
        }*/

        // Show the popup menu
        popupMenu.show()
    }

}