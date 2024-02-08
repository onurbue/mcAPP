package com.example.projetocma.museu

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContentProviderCompat
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentMuseuDetailBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.mapbox.common.MapboxOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
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
        configMap(latitude,longitude,mapView)
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

    fun configMap(latitude: Double?, longitude: Double?, mapView: MapView) {

        mapView?.mapboxMap?.loadStyle(
            Style.MAPBOX_STREETS
        ) { addAnnotationToMap(latitude, longitude, mapView) }

        mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(com.mapbox.geojson.Point.fromLngLat(longitude!!, latitude!!))
                .pitch(3.0)
                .zoom(15.0)
                .bearing(0.0)
                .build()
        )
    }


    fun addAnnotationToMap(latitude: Double?, longitude: Double?, mapView: MapView) {
        // Create an instance of the Annotation API and get the PointAnnotationManager.
        bitmapFromDrawableRes(
            requireContext(),
            R.drawable.red_marker
        )?.let { iconBitmap ->
            val annotationApi = mapView.annotations
            val pointAnnotationManager = annotationApi.createPointAnnotationManager()

            val coordinates = Point.fromLngLat(longitude!!, latitude!!)

            // Set options for the resulting symbol layer.
            val pointAnnotationOptions = PointAnnotationOptions()
                // Define a geographic coordinate.
                .withPoint(coordinates)
                // Specify the bitmap you assigned to the point annotation
                // The bitmap will be added to map style automatically.
                .withIconImage(iconBitmap)

            // Add the resulting pointAnnotation to the map.
            val annotation = pointAnnotationManager?.create(pointAnnotationOptions)

            // Set up a click listener for the annotation
            annotation?.let { pointAnnotation ->
                pointAnnotationManager.clickListeners.add { clickedAnnotation ->
                    if (clickedAnnotation == pointAnnotation) {
                        // Handle the click event
                        val gmmIntentUri =
                            Uri.parse("google.navigation:q=${coordinates.latitude()},${coordinates.longitude()}&mode=d")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        startActivity(mapIntent)
                        true
                    } else {
                        false
                    }
                }

            }

        }
    }

    fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
            // copying drawable object to not manipulate on the same reference
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }


}


