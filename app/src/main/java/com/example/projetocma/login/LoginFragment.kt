package com.example.projetocma.login

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projetocma.NavBar
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    private  var _binding: FragmentLoginBinding? = null
    private lateinit var auth: FirebaseAuth

    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        auth = Firebase.auth

        binding.notRegiste.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_register_fragment)
        }

        binding.ButtonLogin.setOnClickListener {
            val email = binding.EditTextEmail.text.toString()
            val password = binding.EditTextPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "signInWithEmail:success")
                            val user = auth.currentUser
                            findNavController().navigate(R.id.action_loginFragment_to_museusPageFrag)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                context,
                                "Authentication Failed",
                                Toast.LENGTH_SHORT,
                            ).show()

                        }
                    }
            }else{
                Toast.makeText(
                    context,
                    "Credentials missing",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }


        return binding.root
    }



}
