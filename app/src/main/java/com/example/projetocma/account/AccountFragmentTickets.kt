package com.example.projetocma.account

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentAccountTicketsBinding
import com.example.projetocma.databinding.TicketGridItemBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.ktx.storage
import com.google.rpc.context.AttributeContext.Auth
import models.Museu
import models.TicketsComprados
import java.io.ByteArrayOutputStream

class AccountFragmentTickets : Fragment() {
    private lateinit var binding: FragmentAccountTicketsBinding
    var ticketsComprados = arrayListOf<TicketsComprados>()
    private val imageCache = mutableMapOf<String, Bitmap?>()
    private var adapter = TicketsCompradosAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountTicketsBinding.inflate(inflater, container, false)

        binding.listViewBilhetes.adapter = adapter
        val userId = Firebase.auth.currentUser

        val db = Firebase.firestore

        db.collection("bilhetesUser").whereEqualTo("userId", userId?.uid )
            .addSnapshotListener { snapshoot, error ->
                snapshoot?.documents?.let {
                    this.ticketsComprados.clear()
                    for (document in it) {
                        document.data?.let { data ->
                            this.ticketsComprados.add(
                                TicketsComprados.fromSnapshot(
                                    document.id,
                                    data
                                )
                            )
                        }
                    }
                    this.adapter.notifyDataSetChanged()
                }
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
            rootView.ticketPrice.text = ticketsComprados[position].price

            ticketsComprados[position].pathToImg?.let { imagePath ->
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
                val selectedTicket = ticketsComprados[position]
                val imageBitmap = imageCache[selectedTicket.pathToImg]
                if (imageBitmap != null) {
                    val stream = ByteArrayOutputStream()
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val imageByteArray = stream.toByteArray()

                    val bundle = Bundle().apply {
                        putString("name", selectedTicket.name)
                        putByteArray("image", imageByteArray)
                        putString("description", selectedTicket.description)
                        putString("ticketId", ticketsComprados[position].id)
                        putString("price", ticketsComprados[position].price)
                        putString("date", ticketsComprados[position].date)
                    }
                    findNavController().navigate(R.id.ticketInfo, bundle)
                }
            }

            binding.backIconeTickets.setOnClickListener {
                findNavController().popBackStack()
            }


            return rootView.root
        }
    }
}
