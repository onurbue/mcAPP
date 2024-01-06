package com.example.projetocma.museu

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentMuseuDetailBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MuseuDetail : Fragment() {

    private lateinit var binding: FragmentMuseuDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMuseuDetailBinding.inflate(inflater, container, false)

        val navBar =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        navBar.visibility = View.VISIBLE

        // Retrieve arguments
        val name = arguments?.getString("name")
        val description = arguments?.getString("description")
        val imageByteArray = arguments?.getByteArray("image")
        val museuId = arguments?.getString("museuId")
        binding.textViewMuseumName.text = name

        if (imageByteArray != null) {
            val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
            binding.museumImage.setImageBitmap(bitmap)
        } else {
            // Handle the case where imageByteArray is null (e.g., show a default image)
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



}