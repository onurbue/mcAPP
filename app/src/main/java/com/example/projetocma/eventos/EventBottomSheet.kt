package com.example.projetocma.eventos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.projetocma.databinding.FragmentEventBottomSheetBinding
import com.example.projetocma.databinding.FragmentEventDetailBinding


class EventBottomSheet : Fragment() {
    private lateinit var binding: FragmentEventBottomSheetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventBottomSheetBinding.inflate(inflater, container, false)



        return binding.root

    }




}
