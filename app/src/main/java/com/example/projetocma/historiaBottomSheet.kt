package com.example.projetocma

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.projetocma.databinding.FragmentHistoriaBottomSheetBinding


class historiaBottomSheet : Fragment() {

    private lateinit var binding: FragmentHistoriaBottomSheetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoriaBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

}