package com.example.projetocma.tickets

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentTicketsBinding
import com.example.projetocma.databinding.TicketGridItemBinding
import com.example.projetocma.room.AppDatabase
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.ktx.storage
import models.Eventos
import models.Obras
import models.Tickets
import models.Utility
import java.io.ByteArrayOutputStream
import java.util.Date

class TicketsFragment : Fragment() {
    private var _binding: FragmentTicketsBinding? = null
    var tickets = arrayListOf<Tickets>()
    private var adpapter = TicketsAdapter()
    private var museuId: String? = null
    var appDatabase : AppDatabase? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        museuId = arguments?.getString("museuId")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        _binding = FragmentTicketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.listView.adapter = adpapter
        appDatabase = AppDatabase.getDatabase(requireContext())


        if(appDatabase != null){
            Tickets.fetchTickets(museuId!!){
                val ticketsWithMuseumId = it.map { ticket ->
                    ticket.copy(museuId = museuId)
                }
                appDatabase!!.ticketsDao().insertTicketsList(ticketsWithMuseumId)
            }
            appDatabase!!.ticketsDao().getMuseumTickets(museuId!!).observe(viewLifecycleOwner, Observer {
                tickets = it as ArrayList<Tickets>
                adpapter.notifyDataSetChanged()
            })
        }

        binding.setaBackk.setOnClickListener {
            findNavController().popBackStack()
        }

    }


    inner class TicketsAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return tickets.size
        }

        override fun getItem(position: Int): Any {
            return tickets[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        @SuppressLint("SetTextI18n")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val rootView: TicketGridItemBinding

            if (convertView == null) {
                rootView = TicketGridItemBinding.inflate(layoutInflater, parent, false)
            } else {
                rootView = TicketGridItemBinding.bind(convertView)
            }
            rootView.ticketName.text = tickets[position].name
            rootView.ticketPrice.text = tickets[position].price + "€"

            Utility.setImage(tickets[position].pathToImg, rootView.ticketImg, requireContext())

            rootView.root.setOnClickListener {
                val selectedTicket = tickets[position]


                    val bundle = Bundle().apply {
                        putString("name", selectedTicket.name)
                        putString("pathToImage", selectedTicket.pathToImg)
                        putString("description", selectedTicket.description)
                        putString("ticketId", tickets[position].id)
                        putString("price", tickets[position].price)
                        putString("museuId", museuId)
                    }
                    findNavController().navigate(R.id.action_fragmentTickets_to_calendario, bundle)
                }


            return rootView.root
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
