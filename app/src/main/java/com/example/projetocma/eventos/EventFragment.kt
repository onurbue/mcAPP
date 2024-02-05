package com.example.projetocma.eventos

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.room.InvalidationTracker
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentEventBinding
import com.example.projetocma.databinding.GridEventItemBinding
import com.example.projetocma.room.AppDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.ktx.storage
import models.Eventos
import models.Museu
import models.Utility
import java.io.ByteArrayOutputStream


class EventFragment : Fragment() {
    var eventos = arrayListOf<Eventos>()
    private var adpapter = EventosAdapter()
    var museuId: String? = null
    private  var _binding: FragmentEventBinding? = null

    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         museuId = arguments?.getString("museuId")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventBinding.inflate(inflater, container, false)

        val appDatabase = AppDatabase.getDatabase(requireContext())
        binding.gridViewEventos.adapter = adpapter
        val navBar =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        navBar.visibility = View.VISIBLE


        if(appDatabase != null){
            Eventos.fetchEventos(museuId){
                val eventsWithMuseumId = it.map {
                    it.copy(museuId = museuId)
                }
                appDatabase.eventosDao().insertEventosList(eventsWithMuseumId)
            }
            appDatabase.eventosDao().getMuseumEvents(museuId!!).observe(viewLifecycleOwner, Observer {
                    eventos = it as ArrayList<Eventos>
                    adpapter.notifyDataSetChanged()
                })
        }



        binding.setaBackk.setOnClickListener {
            findNavController().popBackStack()
        }


        return binding.root
    }


    inner class EventosAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return eventos.size
        }

        override fun getItem(position: Int): Any {
            return eventos[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

            val rootView: GridEventItemBinding = if (convertView == null) {
                GridEventItemBinding.inflate(layoutInflater, parent, false)
            } else {
                GridEventItemBinding.bind(convertView)
            }

            rootView.textViewEventTitle.text = eventos[position].name
            rootView.textViewEventDate.text = eventos[position].date
            Utility.setImage(eventos[position].image, rootView.eventImage, requireContext())

            rootView.root.setOnClickListener {
                    val bundle = Bundle().apply {
                        putString("description", eventos[position].description)
                        putString("imgDescription", eventos[position].imgDescription)
                        putString("image", eventos[position].image)
                    }
                    findNavController().navigate(R.id.action_eventFragment_to_eventDetail, bundle)

            }

            return rootView.root
        }

    }
}