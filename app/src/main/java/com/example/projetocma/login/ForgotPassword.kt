package com.example.projetocma.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth


class ForgotPassword : Fragment() {
    private  var _binding: FragmentForgotPasswordBinding? = null
    private var auth : FirebaseAuth? = null

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
        _binding = FragmentForgotPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()



        binding.buttonForgotPassword.setOnClickListener {
            val email = binding.EditTextEmail.text.toString()

            auth?.sendPasswordResetEmail(email)
                ?.addOnSuccessListener {
                    Toast.makeText(requireContext(), "Por favor abra o email", Toast.LENGTH_SHORT).show()
                }
                ?.addOnFailureListener {
                    Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
                }
            findNavController().navigate(R.id.action_forgotPassword_to_loginFragment)
        }
    }

}