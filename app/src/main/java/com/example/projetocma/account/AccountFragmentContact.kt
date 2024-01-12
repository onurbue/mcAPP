package com.example.projetocma.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentAccountContactBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import models.TicketsComprados
import models.User

class AccountFragmentContact : Fragment() {

    private lateinit var binding : FragmentAccountContactBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountContactBinding.inflate(layoutInflater)

        val userId = Firebase.auth.currentUser?.uid

        val db = Firebase.firestore

        if (userId != null) {
            val db = Firebase.firestore

            db.collection("User").document(userId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        // Handle the error
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val data = snapshot.data
                        if (data != null) {
                            var user = User.fromSnapshot(userId, data)
                            binding.nome.text = user.username
                            binding.nome2.text = user.email
                            binding.nome3.text = user.phoneNumber
                        }
                    } else {

                    }
                }
        }

        binding.backIconeContact.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

}