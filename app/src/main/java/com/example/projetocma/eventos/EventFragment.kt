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
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentEventBinding
import com.example.projetocma.databinding.GridEventItemBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.ktx.storage
import models.Eventos
import java.io.ByteArrayOutputStream


class EventFragment : Fragment() {
    var eventos = arrayListOf<Eventos>()
    private var adpapter = EventosAdapter()
    private val imageCache = mutableMapOf<String, Bitmap?>()

    private lateinit var binding: FragmentEventBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventBinding.inflate(inflater, container, false)
        binding.gridViewEventos.adapter = adpapter
        val navBar =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        navBar.visibility = View.VISIBLE

        val museuId = arguments?.getString("museuId")
        Log.e("event", "museu id: ${museuId}")

        val db = Firebase.firestore

        db.collection("museus").document(museuId!!).collection("eventos")
            .addSnapshotListener { snapshoot, error ->
                snapshoot?.documents?.let {
                    this.eventos.clear()
                    for (document in it) {
                        document.data?.let { data ->
                            this.eventos.add(
                                Eventos.fromSnapshot(
                                    document.id,
                                    data
                                )
                            )
                        }
                    }
                    this.adpapter.notifyDataSetChanged()
                }
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
            val rootView: GridEventItemBinding

            if (convertView == null) {
                rootView = GridEventItemBinding.inflate(layoutInflater, parent, false)
            } else {
                rootView = GridEventItemBinding.bind(convertView)
            }

            rootView.textViewEventTitle.text = eventos[position].name
            rootView.textViewEventDate.text = eventos[position].date

            eventos[position].image?.let { imagePath ->
                if (imageCache.containsKey(imagePath)) {
                    // Image is in cache, reuse it
                    rootView.eventImage.setImageBitmap(imageCache[imagePath])
                } else {
                    // Image not in cache, retrieve it from Firebase Storage
                    val storage = com.google.firebase.ktx.Firebase.storage
                    val storageRef = storage.reference
                    val pathReference = storageRef.child(imagePath)
                    val ONE_MEGABYTE: Long = 10 * 1024 * 1024

                    pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { data ->
                        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.count())
                        rootView.eventImage.setImageBitmap(bitmap)

                        // Cache the retrieved image
                        imageCache[imagePath] = bitmap
                    }.addOnFailureListener { exception ->
                        // Log the exception message for debugging
                        Log.e("FirebaseStorage", "Image retrieval failed: ${exception.message}")
                        // Handle any errors, e.g., set a default image
                        rootView.eventImage.setImageResource(R.drawable.default_image)
                    }
                }
            }

            rootView.root.setOnClickListener {

                val selectedEvento = eventos[position]
                val imageBitmap = imageCache[selectedEvento.image]
                if (imageBitmap != null) {
                    val stream = ByteArrayOutputStream()
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val imageByteArray = stream.toByteArray()

                    val bundle = Bundle().apply {
                        putString("description", eventos[position].description)
                        putString("imgDescription", eventos[position].imgDescription)
                        putByteArray("image", imageByteArray)
                    }
                    findNavController().navigate(R.id.eventDetail, bundle)
                }
            }

            return rootView.root
        }

    }
}