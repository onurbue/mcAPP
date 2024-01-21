package com.example.projetocma.museu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.projetocma.NavBar
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentMuseusPageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.rpc.context.AttributeContext


class MuseusPageFrag : Fragment() {
    private var _binding: FragmentMuseusPageBinding? = null
    var userId = Firebase.auth.currentUser?.uid
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("MuseusPageFrag", "User ID: $userId")

        _binding = FragmentMuseusPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navBar =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        navBar.visibility = View.GONE

        binding.LoginButton.setOnClickListener{
            if (userId == null){
                findNavController().navigate(R.id.loginFragment)
            }else{
                Toast.makeText(requireContext(), "Já fez o login", Toast.LENGTH_SHORT).show()
            }

        }

        // Set up click listener for the button
        binding.exploreButton.setOnClickListener {
            // Navigate to the destination fragment
            findNavController().navigate(R.id.museusExplore)
            val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
            navBar.menu.findItem(R.id.home).isChecked = true

        }
        binding.qrCodeButton.setOnClickListener {
            // Navigate to the destination fragment
            findNavController().navigate(R.id.qrCode)
            val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
            navBar.menu.findItem(R.id.qrCode).isChecked = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Set the binding to null to release resources
    }
}