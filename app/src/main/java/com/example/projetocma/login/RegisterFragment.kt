import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentRegisterFragmentBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore
import models.User
import models.Utility

class RegisterFragment : Fragment() {

    private  var _binding: FragmentRegisterFragmentBinding? = null
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
        _binding = FragmentRegisterFragmentBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

        binding.buttonRegister.setOnClickListener {
            val email = binding.EditTextEmail.text.toString()
            val password = binding.editTextRegisterPassword.text.toString()
            val username = binding.EditTextRegisterUser.text.toString()
            val phoneNumber = binding.editTextRegisterTelefone.text.toString()

            if (!Utility.isValidEmail(email)) {
                Toast.makeText(
                    context,
                    "Invalid email format. Please include '@' and '.com'",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Validate username length
            if (!Utility.isValidUsername(username)) {
                Toast.makeText(
                    context,
                    "Username must be between 6 and 16 characters",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Validate phone number length
            if (!Utility.isValidPhoneNumber(phoneNumber)) {
                Toast.makeText(
                    context,
                    "Phone number must have 9 digits",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            binding.buttonRegister.setOnClickListener {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Log.d("RegisterFragment", "createUserWithEmail:success")
                        val user: FirebaseUser? = auth.currentUser
                            val newUser = User(
                                id = "" ,// You can leave this empty as it will be generated by Firebase
                                username = username, // Replace with the actual username
                                email = email,
                                phoneNumber = phoneNumber // Replace with the actual phone number
                            )
                            val userMap = newUser.toHashMap()

                        db.collection("User").document(user?.uid ?: "")
                            .set(userMap)
                            .addOnSuccessListener {
                                Log.d("RegisterFragment", "addUser:success")
                            }
                            .addOnFailureListener { e ->
                                Log.w("RegisterFragment", "addUser:failure", e)
                            }

                        findNavController().navigate(R.id.action_register_fragment_to_loginFragment)
                    } else {
                        Log.w("RegisterFragment", "createUserWithEmail:failure", task.exception)
                    }
                }
            }
        }

        return binding.root
    }



}
