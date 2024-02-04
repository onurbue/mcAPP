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

    private  var _binding: FragmentAccountNotLoggedBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountNotLoggedBinding.inflate(layoutInflater)

        binding.constrainMetodoPagamento1.setOnClickListener {
            findNavController().navigate(R.id.action_accountNotLogged_to_loginFragment)
        }
        return binding.root
    }

}