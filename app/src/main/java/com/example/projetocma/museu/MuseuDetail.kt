package com.example.projetocma.museu

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
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
import com.google.firebase.firestore.firestore
import com.mapbox.common.MapboxOptions
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView

class MuseuDetail : Fragment() {

    private var _binding: FragmentMuseuDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView: MapView
    var userId = Firebase.auth.currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapboxOptions.accessToken = "pk.eyJ1IjoibWFudWNhMTciLCJhIjoiY2xyMWQ2Z2l1MDEwYjJsbW1zZjF6cGVkMiJ9.oxptdQODQ7e_wqemar23Bw"

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

        // Retrieve arguments
        val name = arguments?.getString("name")
        val description = arguments?.getString("description")
        val imageByteArray = arguments?.getByteArray("image")
        val museuId = arguments?.getString("museuId")
        val latitude = arguments?.getDouble("latitude")
        val longitude = arguments?.getDouble("longitude")
        binding.textViewMuseumName.text = name

        configMap(latitude,longitude,mapView)

        if (imageByteArray != null) {
            val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
            binding.museumImage.setImageBitmap(bitmap)
        } else {
            binding.museumImage.setImageResource(R.drawable.default_image)
        }




        binding.historiaButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("name", name)
            bundle.putByteArray("image", imageByteArray)
            bundle.putString("description", description)

            findNavController().navigate(R.id.historiaMuseu, bundle)
        }
        binding.obrasButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("museuId", museuId)
            findNavController().navigate(R.id.obrasExplore, bundle)
        }

        binding.eventosButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("museuId", museuId)
            findNavController().navigate(R.id.eventFragment, bundle)
        }
        //asaadsadsadsad
        binding.setaBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.reserveMuseumCodeButton.setOnClickListener {
            if (userId == null){
                Toast.makeText(requireContext(), "Faça o login primeiro", Toast.LENGTH_SHORT).show()
            }else{
                val bundle = Bundle()
                bundle.putString("museuId", museuId)
                findNavController().navigate(R.id.fragmentTickets, bundle)
            }

        }

        return binding.root

    }

    private fun configMap(latitude: Double?, longitude: Double?, mapView: MapView) {

        mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(com.mapbox.geojson.Point.fromLngLat(longitude!!, latitude!!))
                .pitch(3.0)
                .zoom(15.0)
                .bearing(0.0)
                .build()
        )
    }

    private fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Set the binding to null to release resources
    }


}