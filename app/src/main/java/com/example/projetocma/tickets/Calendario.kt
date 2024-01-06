package com.example.projetocma.tickets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentCalendarioBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class Calendario : Fragment() {
    private var _binding: FragmentCalendarioBinding? = null
    private val binding get() = _binding!!

    private var selectedDate: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        navBar.visibility = View.GONE
        _binding = FragmentCalendarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            val locale = Locale("pt")
            val dateFormat = java.text.SimpleDateFormat("dd MMMM yyyy", locale)
            selectedDate = calendar.time
            val date = dateFormat.format(calendar.time)

            // Você pode realizar qualquer ação com a data selecionada aqui
        }

        binding.buttonNextEvent.setOnClickListener {
            val name = arguments?.getString("name")
            val imageResId = arguments?.getByteArray("image")
            val description = arguments?.getString("description")
            val price = arguments?.getString("price")
            val pathToImage = arguments?.getString("pathToImage")
            val bundle = Bundle()
            bundle.putSerializable("selectedDate", selectedDate)
            bundle.putString("name", name)
            bundle.putByteArray("image", imageResId)
            bundle.putString("description", description)
            bundle.putString("price", price)
            bundle.putString("pathToImage", pathToImage)



            // Navegação para o fragmento TimePicker com o Bundle contendo a data selecionada
            findNavController().navigate(R.id.ticketBasicc, bundle)
        }

        binding.buttonBackEvent.setOnClickListener {
            // Faça qualquer coisa que desejar ao clicar no botão "Anterior"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getSelectedDate(): Date? {
        return selectedDate
    }
}
