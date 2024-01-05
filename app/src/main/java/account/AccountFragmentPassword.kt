package account

import android.os.Bundle
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

class AccountFragmentPassword : Fragment() {
    private lateinit var binding: FragmentAccountPasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountPasswordBinding.inflate(inflater, container, false)

        binding.backIconePassword.setOnClickListener {
            findNavController().navigate(R.id.accountFragment)
        }

        binding.guardar.setOnClickListener {
            findNavController().navigate(R.id.accountFragment)
            showToast("A palavra passe foi alterada com sucesso")
        }



        return binding.root
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
