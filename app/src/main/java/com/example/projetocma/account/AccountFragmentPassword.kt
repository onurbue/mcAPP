package com.example.projetocma.account

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentAccountBinding
import com.example.projetocma.databinding.FragmentAccountPasswordBinding
import com.example.projetocma.databinding.FragmentEventBottomSheetBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class AccountFragmentPassword : Fragment() {
    private lateinit var binding: FragmentAccountPasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountPasswordBinding.inflate(inflater, container, false)

        val user = Firebase.auth.currentUser


        binding.backIconePassword.setOnClickListener {
            findNavController().navigate(R.id.accountFragment)
        }

        binding.guardar.setOnClickListener {
            var newPass =  binding.editTextPalavraPasseNova.text.toString()
            var verifyPass = binding.editTextVerificarPalavraPasse.text.toString()
            if (newPass == verifyPass) {
                user!!.updatePassword(newPass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User password updated.")
                        }
                    }
                findNavController().navigate(R.id.accountFragment)
                showToast("A palavra passe foi alterada com sucesso")
            } else {
                showToast("A palavra passe não coincide")
            }
        }
        return binding.root
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
