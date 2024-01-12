package com.example.projetocma.tickets

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentTicketsBinding
import com.example.projetocma.databinding.TicketGridItemBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.ktx.storage
import models.Tickets
import java.io.ByteArrayOutputStream

class TicketsFragment : Fragment() {
    private lateinit var binding: FragmentTicketsBinding
    var tickets = arrayListOf<Tickets>()
    private var adpapter = TicketsAdapter()
    private val imageCache = mutableMapOf<String, Bitmap?>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTicketsBinding.inflate(inflater, container, false)

        val museuId = arguments?.getString("museuId")
        binding.listView.adapter = adpapter


        val db = Firebase.firestore

        db.collection("museus").document(museuId!!).collection("tickets")
            .addSnapshotListener { snapshoot, error ->
                snapshoot?.documents?.let {
                    this.tickets.clear()
                    for (document in it) {
                        document.data?.let { data ->
                            this.tickets.add(
                                Tickets.fromSnapshot(
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

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val rootView: TicketGridItemBinding

            if (convertView == null) {
                rootView = TicketGridItemBinding.inflate(layoutInflater, parent, false)
            } else {
                rootView = TicketGridItemBinding.bind(convertView)
            }
            rootView.ticketName.text = tickets[position].name
            rootView.ticketPrice.text = tickets[position].price + "€"

            tickets[position].pathToImg?.let { imagePath ->
                if (imageCache.containsKey(imagePath)) {
                    // Image is in cache, reuse it
                    rootView.ticketImg.setImageBitmap(imageCache[imagePath])
                } else {
                    // Image not in cache, retrieve it from Firebase Storage
                    val storage = com.google.firebase.ktx.Firebase.storage
                    val storageRef = storage.reference
                    val pathReference = storageRef.child(imagePath)
                    val ONE_MEGABYTE: Long = 10 * 1024 * 1024

                    pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { data ->
                        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.count())
                        rootView.ticketImg.setImageBitmap(bitmap)

                        // Cache the retrieved image
                        imageCache[imagePath] = bitmap
                    }.addOnFailureListener { exception ->
                        // Log the exception message for debugging
                        Log.e("FirebaseStorage", "Image retrieval failed: ${exception.message}")
                        // Handle any errors, e.g., set a default image
                        rootView.ticketImg.setImageResource(R.drawable.default_image)
                    }
                }
            }

            rootView.root.setOnClickListener {
                val selectedTicket = tickets[position]
                val imageBitmap = imageCache[selectedTicket.pathToImg]
                if (imageBitmap != null) {
                    val stream = ByteArrayOutputStream()
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val imageByteArray = stream.toByteArray()

                    val bundle = Bundle().apply {
                        putString("name", selectedTicket.name)
                        putString("pathToImage", selectedTicket.pathToImg)
                        putByteArray("image", imageByteArray)
                        putString("description", selectedTicket.description)
                        putString("ticketID", tickets[position].id)
                        putString("price", tickets[position].price)
                    }
                    findNavController().navigate(R.id.calendario, bundle)
                }
            }


            return rootView.root
        }
    }
}
