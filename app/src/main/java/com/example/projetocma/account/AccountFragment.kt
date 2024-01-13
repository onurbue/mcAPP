package com.example.projetocma.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentAccountBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private lateinit var auth: FirebaseAuth
    var userId = Firebase.auth.currentUser?.uid


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()


        if (userId == null){
            findNavController().navigate(R.id.accountNotLogged)
        }

        binding.constrainMetodoPagamento1.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.museusPageFrag)
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