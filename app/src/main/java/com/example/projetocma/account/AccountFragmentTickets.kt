package com.example.projetocma.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentAccountTicketsBinding
import com.example.projetocma.databinding.TicketGridItemBinding
import com.example.projetocma.room.AppDatabase
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import models.Tickets
import models.TicketsComprados
import models.Utility

class AccountFragmentTickets : Fragment() {
    private  var _binding: FragmentAccountTicketsBinding? = null
    var ticketsComprados = arrayListOf<TicketsComprados>()
    private var adapter = TicketsCompradosAdapter()

    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountTicketsBinding.inflate(inflater, container, false)

        binding.listViewBilhetes.adapter = adapter
        val userId = Firebase.auth.currentUser


        val appDatabase = AppDatabase.getDatabase(requireContext())

        if(appDatabase != null){
            TicketsComprados.getTicketsComprados(userId?.uid!!){
                appDatabase.ticketsCompradosDao().insertTicketsCompradosList(it)
            }
            appDatabase.ticketsCompradosDao().getAll().observe(viewLifecycleOwner, Observer {
                ticketsComprados = it as ArrayList<TicketsComprados>
                adapter.notifyDataSetChanged()
            })
        }

        binding.backIconeTickets.setOnClickListener {
            findNavController().popBackStack()
        }




        return binding.root
    }

    inner class TicketsCompradosAdapter : BaseAdapter() {
        override fun getCount(): Int {
           return ticketsComprados.size
        }

        override fun getItem(position: Int): Any {
            return ticketsComprados[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rootView: TicketGridItemBinding

            if (convertView == null) {
                rootView = TicketGridItemBinding.inflate(layoutInflater, parent, false)
            } else {
                rootView = TicketGridItemBinding.bind(convertView)
            }
            rootView.ticketName.text = ticketsComprados[position].name
            rootView.ticketPrice.text = ticketsComprados[position].price + "€"
            Utility.setImage(ticketsComprados[position].pathToImg, rootView.ticketImg, requireContext())

            rootView.root.setOnClickListener {
                    val bundle = Bundle().apply {
                        putString("name", ticketsComprados[position].name)
                        putString("image", ticketsComprados[position].pathToImg)
                        putString("description", ticketsComprados[position].description)
                        putString("ticketId", ticketsComprados[position].id)
                        putString("price", ticketsComprados[position].price)
                        putString("date", ticketsComprados[position].date)
                    }
                    findNavController().navigate(R.id.action_accountTickets_to_ticketInfo, bundle)

            }

            binding.backIconeTickets.setOnClickListener {
                findNavController().popBackStack()
            }


            return rootView.root
        }
    }
}
