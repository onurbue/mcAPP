package com.example.projetocma.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projetocma.MainActivity

import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentAccountBinding
import com.example.projetocma.databinding.FragmentQrCodeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore


class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()

        binding.constrainMetodoPagamento1.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }

        binding.constraintContacto.setOnClickListener {
            findNavController().navigate(R.id.perfil)
        }

        binding.constraintMudarPassword.setOnClickListener {
            findNavController().navigate(R.id.accountPassword)
        }

        binding.constrainMetodoPagamento.setOnClickListener {
            findNavController().navigate(R.id.accountTickets)
        }

        return binding.root
    }


}