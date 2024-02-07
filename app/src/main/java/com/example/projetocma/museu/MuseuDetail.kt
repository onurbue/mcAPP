package com.example.projetocma.museu

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentMuseuDetailBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.mapbox.common.MapboxOptions
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import models.Museu
import models.Utility

class MuseuDetail : Fragment() {

    private var name: String? = null
    private var description: String? = null
    private var imagePath: String? = null
    private var museuId: String? = null
    private var latitude: Double? = null
    private var longitude: Double? = null

    private var _binding: FragmentMuseuDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView: MapView
    var userId = Firebase.auth.currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapboxOptions.accessToken = "pk.eyJ1IjoibWFudWNhMTciLCJhIjoiY2xyMWQ2Z2l1MDEwYjJsbW1zZjF6cGVkMiJ9.oxptdQODQ7e_wqemar23Bw"
         name = arguments?.getString("name")
         description = arguments?.getString("description")
         imagePath = arguments?.getString("image")
         museuId = arguments?.getString("museuId")
         latitude = arguments?.getDouble("latitude")
         longitude = arguments?.getDouble("longitude")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMuseuDetailBinding.inflate(inflater, container, false)
        mapView = binding.appCompatImageView2
        val navBar =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        navBar.visibility = View.VISIBLE


        Museu.updateQuantityClicked(museuId!!)
        binding.textViewMuseumName.text = name
        Utility.configMap(latitude,longitude,mapView)
        Utility.setImage(imagePath, binding.museumImage, requireContext())

        binding.historiaButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("name", name)
            bundle.putString("image", imagePath)
            bundle.putString("description", description)

            findNavController().navigate(R.id.action_museuDetail_to_historiaMuseu, bundle)
        }
        binding.obrasButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("museuId", museuId)
            findNavController().navigate(R.id.action_museuDetail_to_obrasExplore, bundle)
        }

        binding.eventosButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("museuId", museuId)
            findNavController().navigate(R.id.action_museuDetail_to_eventFragment, bundle)
        }
        binding.setaBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.reserveMuseumCodeButton.setOnClickListener {
            if (userId == null){
                findNavController().navigate(R.id.loginFragment)
            }else{
                val bundle = Bundle()
                bundle.putString("museuId", museuId)
                findNavController().navigate(R.id.action_museuDetail_to_fragmentTickets, bundle)
            }

        }

        return binding.root

    }



    private fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Set the binding to null to release resources
    }


}