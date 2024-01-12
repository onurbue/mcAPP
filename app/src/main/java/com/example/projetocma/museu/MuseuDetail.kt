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
import com.google.firebase.firestore.firestore
import com.mapbox.common.MapboxOptions
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import models.Location

class MuseuDetail : Fragment() {

    private lateinit var binding: FragmentMuseuDetailBinding
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapboxOptions.accessToken = "pk.eyJ1IjoibWFudWNhMTciLCJhIjoiY2xyMWQ2Z2l1MDEwYjJsbW1zZjF6cGVkMiJ9.oxptdQODQ7e_wqemar23Bw"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMuseuDetailBinding.inflate(inflater, container, false)
        mapView = binding.appCompatImageView2


        val navBar =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        navBar.visibility = View.VISIBLE

        // Retrieve arguments
        val name = arguments?.getString("name")
        val description = arguments?.getString("description")
        val imageByteArray = arguments?.getByteArray("image")
        val museuId = arguments?.getString("museuId")
        binding.textViewMuseumName.text = name

        val db = Firebase.firestore
        db.collection("Location")
            .whereEqualTo("museuId", museuId)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.first()
                    val location = Location.fromSnapshot(document.id, document.data)
                    configMap(location, mapView)
                } else {
                    showToast("No document found", requireContext())
                }
            }
            .addOnFailureListener {
                showToast("An error occurred: ${it.localizedMessage}", requireContext())
            }

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

        binding.setaBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.reserveMuseumCodeButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("museuId", museuId)
            findNavController().navigate(R.id.fragmentTickets, bundle)
        }

        return binding.root


    }

    private fun configMap(location: models.Location, mapView: MapView) {

        mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(com.mapbox.geojson.Point.fromLngLat(location.longitude, location.latitude))
                .pitch(3.0)
                .zoom(15.0)
                .bearing(0.0)
                .build()
        )
    }

    private fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


}