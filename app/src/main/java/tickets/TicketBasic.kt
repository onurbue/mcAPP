package tickets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentTicketBasiccBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class TicketBasic : Fragment() {
    private lateinit var binding: FragmentTicketBasiccBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTicketBasiccBinding.inflate(inflater, container, false)

        val name = arguments?.getString("name")
        val description = arguments?.getString("description")
        val imageResId = arguments?.getInt("image")
        val price = arguments?.getString("price")
        val selectedDate: Date? = arguments?.getSerializable("selectedDate") as? Date

        binding.ticketPrice.text = price
        binding.ticketName.text = name
        binding.description.text = description
        imageResId?.let { binding.ticketImg.setImageResource(it) }

        if (selectedDate != null) {
            val formattedDate = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(selectedDate)
            binding.data.text = formattedDate
        }

        binding.ticketNamesPrice.text = price.toString()


        binding.buttonNextBasic.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("price", price)
            findNavController().navigate(R.id.museusExplore)

            showToast("O pagamento foi bem sucedido")
        }

        binding.setaBackk.setOnClickListener {
            findNavController().popBackStack()
        }


        return binding.root
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}