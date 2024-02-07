package com.example.projetocma.tickets

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentCalendarioBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.Clock
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.text.SimpleDateFormat


class Calendario : Fragment() {
    private var _binding: FragmentCalendarioBinding? = null
    private val binding get() = _binding!!

    private var selectedDate: Date? = null
    var name : String? = null
    var description : String? = null
    var price : String? = null
    var pathToImage : String? = null
    var museuId : String? = null
    var ticketId : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         name = arguments?.getString("name")
         description = arguments?.getString("description")
         price = arguments?.getString("price")
         pathToImage = arguments?.getString("pathToImage")
         museuId = arguments?.getString("museuId")
         ticketId = arguments?.getString("ticketId")

    }
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            val locale = Locale("pt")
            val dateFormat = java.text.SimpleDateFormat("dd MMMM yyyy", locale)
            selectedDate = calendar.time


            selectedDate = calendar.time

            val date = dateFormat.format(calendar.time)
            val selectedLocalDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd MMMM yyyy", locale))

            if (selectedLocalDate.isBefore(LocalDate.now())) {

                // Bloquear a data


            } else {
                binding.buttonNextEvent.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putSerializable("selectedDate", selectedDate)
                    bundle.putString("name", name)
                    bundle.putString("description", description)
                    bundle.putString("price", price)
                    bundle.putString("pathToImage", pathToImage)
                    bundle.putString("museuId", museuId)
                    bundle.putString("ticketId", ticketId)

                    findNavController().navigate(R.id.action_calendario_to_ticketBasicc, bundle)
                }
            }
        }

        binding.buttonBackEvent.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
