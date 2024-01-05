package com.example.projetocma

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.projetocma.databinding.FragmentMuseusExploreBinding
import com.example.projetocma.databinding.GridItemBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.ktx.storage
import models.Museu
import java.io.ByteArrayOutputStream


class MuseusExplore : Fragment() {

    private lateinit var binding: FragmentMuseusExploreBinding
    var museus = arrayListOf<Museu>()
    private var adpapter = MuseusAdapter()
    private val imageCache = mutableMapOf<String, Bitmap?>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMuseusExploreBinding.inflate(inflater, container, false)
        binding.gridView.adapter = adpapter

        val categoryCard = binding.categoryCard

        categoryCard.setOnClickListener {
            showCategoryMenu(categoryCard)
        }

        val db = Firebase.firestore

        db.collection("museus")
            .addSnapshotListener { snapshoot, error ->
                snapshoot?.documents?.let {
                    this.museus.clear()
                    for (document in it) {
                        document.data?.let { data ->
                            this.museus.add(
                                Museu.fromSnapshot(
                                    document.id,
                                    data
                                )
                            )
                        }
                    }
                    this.adpapter.notifyDataSetChanged()
                }
            }

        val navBar =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        navBar.visibility = View.VISIBLE
        val view = binding.root




        return view
    }


    inner class MuseusAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return museus.size
        }

        override fun getItem(position: Int): Any {
            return museus[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val rootView: GridItemBinding

            if (convertView == null) {
                rootView = GridItemBinding.inflate(layoutInflater, parent, false)
            } else {
                rootView = GridItemBinding.bind(convertView)
            }

            rootView.gridName.text = museus[position].name

            Log.d("FirebaseStorage", "Image Path: ${museus[position].image}")

            museus[position].image?.let { imagePath ->
                if (imageCache.containsKey(imagePath)) {
                    // Image is in cache, reuse it
                    rootView.gridImage.setImageBitmap(imageCache[imagePath])
                } else {
                    // Image not in cache, retrieve it from Firebase Storage
                    val storage = com.google.firebase.ktx.Firebase.storage
                    val storageRef = storage.reference
                    val pathReference = storageRef.child(imagePath)
                    val ONE_MEGABYTE: Long = 10 * 1024 * 1024

                    pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { data ->
                        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.count())
                        rootView.gridImage.setImageBitmap(bitmap)

                        // Cache the retrieved image
                        imageCache[imagePath] = bitmap
                    }.addOnFailureListener { exception ->
                        // Log the exception message for debugging
                        Log.e("FirebaseStorage", "Image retrieval failed: ${exception.message}")
                        // Handle any errors, e.g., set a default image
                        rootView.gridImage.setImageResource(R.drawable.default_image)
                    }
                }
            }


                rootView.root.setOnClickListener {
                    val selectedMuseu = museus[position]
                    val imageBitmap = imageCache[selectedMuseu.image]
                    if (imageBitmap != null) {
                        val stream = ByteArrayOutputStream()
                        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        val imageByteArray = stream.toByteArray()

                        val bundle = Bundle().apply {
                            putString("name", selectedMuseu.name)
                            putByteArray("image", imageByteArray)
                            putString("description", selectedMuseu.description)
                            putString("museuId", museus[position].id)
                        }
                        findNavController().navigate(R.id.museuDetail, bundle)
                    }
                }

                return rootView.root
            }

        }
    private fun showCategoryMenu(anchorView: View) {
        val popupMenu = PopupMenu(requireContext(), anchorView)
        popupMenu.menuInflater.inflate(R.menu.categorias_menu, popupMenu.menu)

        // Set a click listener for each menu item
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_category_art -> filterMuseumsByCategory("Arte")
                R.id.menu_category_history -> filterMuseumsByCategory("Cultura")
                // Add more categories as needed
            }
            true
        }

        // Show the popup menu
        popupMenu.show()
    }

    private fun filterMuseumsByCategory(category: String) {
        getMuseumsByCategory(category) { filteredMuseums ->
            museus = ArrayList(filteredMuseums)
            adpapter.notifyDataSetChanged()
        }
    }

    private fun getMuseumsByCategory(category: String, callback: (List<Museu>) -> Unit) {
        val db = Firebase.firestore

        db.collection("museus")
            .whereEqualTo("categoria", category)
            .addSnapshotListener { snapshot, error ->
                snapshot?.documents?.let {
                    val museums = mutableListOf<Museu>()
                    for (document in it) {
                        document.data?.let { data ->
                            museums.add(
                                Museu.fromSnapshot(
                                    document.id,
                                    data
                                )
                            )
                        }
                    }
                    callback(museums)
                }
            }
    }

    }

