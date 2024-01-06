package com.example.projetocma.tickets

import android.content.ContentValues.TAG
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        binding = FragmentTicketBasiccBinding.inflate(inflater, container, false)

        val name = arguments?.getString("name")
        val description = arguments?.getString("description")
        val image = arguments?.getByteArray("image")
        val price = arguments?.getString("price")
        val pathToImage = arguments?.getString("pathToImage")
        val selectedDate: Date? = arguments?.getSerializable("selectedDate") as? Date

        binding.ticketPrice.text = price
        binding.ticketName.text = name
        binding.description.text = description

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

        val db = Firebase.firestore

        binding.buttonNextBasic.setOnClickListener {
            findNavController().navigate(R.id.museusExplore)

            showToast("O pagamento foi bem sucedido")


            val db = FirebaseFirestore.getInstance()

            db.collection("bilhetesUser")
                .add(ticketMap)
                .addOnSuccessListener { documentReference ->
                    val ticketId = documentReference.id
                }
                .addOnFailureListener { e ->
                }

        }

        binding.setaBackk.setOnClickListener {
            findNavController().popBackStack()
        }


        return binding.root
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}