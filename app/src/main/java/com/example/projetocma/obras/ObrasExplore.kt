package com.example.projetocma.obras

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentObrasExploreBinding
import com.example.projetocma.databinding.GridItemBinding
import com.example.projetocma.room.AppDatabase
import models.Eventos
import models.Obras
import models.Utility

class ObrasExplore : Fragment() {
    var obras = arrayListOf<Obras>()
    private var _binding: FragmentObrasExploreBinding? = null
    private var adpapter = ObrasAdapter()
    var museuId: String? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        museuId = arguments?.getString("museuId")
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


        if(appDatabase != null){
            Obras.fetchObras(museuId){
                appDatabase.obrasDao().insertObrasList(it)
            }
            appDatabase.obrasDao().getAll().observe(viewLifecycleOwner, Observer {
                obras = it as ArrayList<Obras>
                adpapter.notifyDataSetChanged()
            })
        }




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

            val rootView: GridItemBinding = if (convertView == null) {
                GridItemBinding.inflate(layoutInflater, parent, false)
            } else {
                GridItemBinding.bind(convertView)
            }

            rootView.gridName.text = obras[position].name
            Utility.setImage(obras[position].image, rootView.gridImage, requireContext())

                rootView.root.setOnClickListener {
                        val bundle = Bundle().apply {
                            putString("description", obras[position].description)
                            putString("imgDescription", obras[position].imgDescription)
                            putString("name", obras[position].name)
                            putString("image", obras[position].image)
                        }
                        findNavController().navigate(R.id.action_obrasExplore_to_obrasDetail, bundle)
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
