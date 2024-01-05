package com.example.projetocma

import android.content.Context
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
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.projetocma.databinding.FragmentObrasExploreBinding
import com.example.projetocma.databinding.GridEventItemBinding
import com.example.projetocma.databinding.GridItemBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.ktx.storage
import models.Eventos
import models.Museu
import models.Obras
import java.io.ByteArrayOutputStream


class ObrasExplore : Fragment() {
    var obras = arrayListOf<Obras>()
    private lateinit var binding: FragmentObrasExploreBinding
    private var adpapter = ObrasAdapter()
    private val imageCache = mutableMapOf<String, Bitmap?>()

    private val names = arrayOf("A noite estrelada", "O grito", "Abaporu")
    private val img = arrayOf(R.drawable.obra1, R.drawable.obra2, R.drawable.obra3).toIntArray()
    private val description = arrayOf("A Noite Estrelada é uma pintura de Vincent van Gogh de 1889. A obra retrata a vista da janela de um quarto do hospício de Saint-Rémy-de-Provence, pouco antes do nascer do sol, com a adição de um vilarejo idealizado pelo artista. A tela faz parte da coleção permanente do Museu de Arte Moderna de Nova Iorque desde 1941", "asdasdasdasdasd", "asdasdasdasd")
    private val imgDescription = arrayOf("Van Gogh|1889|Oleo sobre a tela|73.7 x 92.1|", "asdasdasdasdasd", "asdasdasdasd")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentObrasExploreBinding.inflate(inflater, container, false)
        binding.gridView.adapter = adpapter
        val museuId = arguments?.getString("museuId")

        val db = Firebase.firestore

        db.collection("museus").document(museuId!!).collection("obras")
            .addSnapshotListener { snapshoot, error ->
                snapshoot?.documents?.let {
                    this.obras.clear()
                    for (document in it) {
                        document.data?.let { data ->
                            this.obras.add(
                                Obras.fromSnapshot(
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


    inner class ObrasAdapter : BaseAdapter(){
        override fun getCount(): Int {
            return obras.size
        }

        override fun getItem(position: Int): Any {
            return obras[position]
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
            rootView.gridName.text = obras[position].name

                obras[position].image?.let { imagePath ->
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
                    rootView.root.setOnClickListener {

                        val selectedObras = obras[position]
                        val imageBitmap = imageCache[selectedObras.image]
                        if (imageBitmap != null) {
                            val stream = ByteArrayOutputStream()
                            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                            val imageByteArray = stream.toByteArray()

                            val bundle = Bundle().apply {
                                putString("description", obras[position].description)
                                putString("imgDescription", obras[position].imgDescription)
                                putString("name", obras[position].name)
                                putByteArray("image", imageByteArray)
                            }
                            findNavController().navigate(R.id.obrasDetail, bundle)
                        }
                    }
                }

            return rootView.root
        }
    }


}