package com.example.projetocma.museu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentMuseusExploreBinding
import com.example.projetocma.databinding.GridItemBinding
import com.example.projetocma.room.AppDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import models.Museu
import models.Utility


class MuseusExplore : Fragment() {

    private  var _binding: FragmentMuseusExploreBinding? = null
    private val binding get() = _binding!!
    var museus = mutableListOf<Museu>()
    private var adpapter = MuseusAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMuseusExploreBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gridView.adapter = adpapter

        val categoryCard = binding.categoryCard

        categoryCard.setOnClickListener {
            showCategoryMenu(categoryCard)
        }

        binding.setaBack.setOnClickListener{
            findNavController().navigate(R.id.action_museusExplore_to_museusPageFrag)
        }

        val navBar =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        navBar.visibility = View.VISIBLE

        val appDatabase = AppDatabase.getDatabase(requireContext())
        if(appDatabase != null){
            Museu.fetchMuseums {
                appDatabase.museuDao().insertMuseuList(it)
            }
            appDatabase.museuDao().getAll().observe(viewLifecycleOwner, Observer {
                museus = it.toMutableList()
                adpapter.notifyDataSetChanged()
            })
        }
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


            Utility.setImage(museus[position].image, rootView.gridImage, requireContext())

            rootView.root.setOnClickListener {
                val selectedMuseu = museus[position]

                    val bundle = Bundle().apply {
                        putString("name", selectedMuseu.name)
                        putString("image", selectedMuseu.image)
                        putString("description", selectedMuseu.description)
                        putString("museuId", museus[position].id)
                        putDouble("latitude", museus[position].latitude)
                        putDouble("longitude", museus[position].longitude)
                    }

                    findNavController().navigate(R.id.action_museusExplore_to_museuDetail, bundle)

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
                    R.id.menu_category_none -> Museu.fetchMuseums {
                        museus.clear()
                        museus.addAll(it)
                        this.adpapter.notifyDataSetChanged()
                    }
                    // Add more categories as needed
                }
                true
            }

            // Show the popup menu
            popupMenu.show()
        }

        private fun filterMuseumsByCategory(category: String) {
            Museu.getMuseumsByCategory(category) { filteredMuseums ->
                museus = ArrayList(filteredMuseums)
                adpapter.notifyDataSetChanged()
            }
        }


        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null // Set the binding to null to release resources
        }



}

