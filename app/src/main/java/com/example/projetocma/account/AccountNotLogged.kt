package com.example.projetocma.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentAccountNotLoggedBinding


class AccountNotLogged : Fragment() {

    private lateinit var binding: FragmentAccountNotLoggedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountNotLoggedBinding.inflate(layoutInflater)

        binding.constrainMetodoPagamento1.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
        return binding.root
    }

}