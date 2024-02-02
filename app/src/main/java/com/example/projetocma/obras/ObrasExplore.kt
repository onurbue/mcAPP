package com.example.projetocma.obras

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
import com.example.projetocma.databinding.FragmentMuseusPageBinding
import com.example.projetocma.databinding.FragmentObrasExploreBinding
import com.example.projetocma.databinding.GridItemBinding
import com.example.projetocma.room.AppDatabase
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.ktx.storage
import models.Eventos
import models.Obras
import java.io.ByteArrayOutputStream

class ObrasExplore : Fragment() {
    var obras = arrayListOf<Obras>()
    private var _binding: FragmentObrasExploreBinding? = null
    private var adpapter = ObrasAdapter()
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentObrasExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appDatabase = AppDatabase.getDatabase(requireContext())
        binding.gridView.adapter = adpapter
        val museuId = arguments?.getString("museuId")


        if (!appDatabase?.obrasDao()?.hasAnyRecord()!!) {
            Obras.fetchObras(museuId) {
                appDatabase.obrasDao().insertObrasList(it)
            }
            val localObras = appDatabase.obrasDao().getAll()
            obras.clear()
            obras.addAll(localObras)
        }else{
            val localObras = appDatabase.obrasDao().getAll()
            obras.clear()
            obras.addAll(localObras)
        }
        adpapter.notifyDataSetChanged()




        binding.setaBackk.setOnClickListener {
            findNavController().popBackStack()
        }


        binding.setaBackk.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    inner class ObrasAdapter : BaseAdapter() {
        val imageCache = mutableMapOf<String, Bitmap?>()

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
                    rootView.gridImage.setImageBitmap(imageCache[imagePath])
                } else {
                    val storage = com.google.firebase.ktx.Firebase.storage
                    val storageRef = storage.reference
                    val pathReference = storageRef.child(imagePath)
                    val ONE_MEGABYTE: Long = 10 * 1024 * 1024

                    pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { data ->
                        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.count())
                        rootView.gridImage.setImageBitmap(bitmap)
                        imageCache[imagePath] = bitmap
                    }.addOnFailureListener { exception ->
                        Log.e("FirebaseStorage", "Image retrieval failed: ${exception.message}")
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

    override fun onDestroyView() {
        super.onDestroyView()
        adpapter.imageCache.clear()
        binding.gridView.adapter = null
        _binding = null
    }

}
