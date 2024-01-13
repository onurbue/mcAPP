package com.example.projetocma

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.projetocma.databinding.FragmentQrCodeBinding
import com.example.projetocma.museu.MuseusExplore
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.BarcodeView
import models.Museu
import models.QrCodeModel

class QrCode : Fragment() {
    private lateinit var binding: FragmentQrCodeBinding
    private lateinit var barcodeView: BarcodeView
    private val MY_PERMISSIONS_CAMERA = 101
    val qrCodeList = arrayListOf<QrCodeModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentQrCodeBinding.inflate(inflater, container, false)
        // Infla o layout do fragmento

        // Encontra o BarcodeView no layout inflado
        barcodeView = binding.barcodeScanner

        // Verifica se a permissão da câmera foi concedida
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Solicita permissão da câmera se ainda não foi concedida
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                MY_PERMISSIONS_CAMERA
            )
        } else {
            startScanning()

        }

        binding.setaBackk2.setOnClickListener {
            findNavController().popBackStack()
            Log.d("QrCodeFragment", "Back button clicked")
        }

        return binding.root
    }

    private fun startScanning() {
        // Configura o BarcodeView para decodificar códigos de barras continuamente
        barcodeView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                result?.let {
                    fetchQrCodes(result)
                }
            }

        })
    }

    override fun onResume() {
        super.onResume()
        // Resume a detecção do código de barras quando o fragmento é retomado
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            barcodeView.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        // Pausa a detecção do código de barras quando o fragmento é pausado
        barcodeView.pause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Verifica se a permissão da câmera foi concedida
        if (requestCode == MY_PERMISSIONS_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Se a permissão foi concedida, inicia a detecção do código de barras
                startScanning()
            } else {
                // Se a permissão foi negada, exibe uma mensagem ao usuário
                Toast.makeText(
                    requireContext(),
                    "Permissão de câmera negada. Não é possível escanear.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun fetchQrCodes(result: BarcodeResult) {
        val db = Firebase.firestore

        db.collection("qrCode")
            .whereEqualTo("qrCodeString", result.text) // Assuming "qrCodeString" is the field name
            .addSnapshotListener { snapshot, error ->
                snapshot?.documents?.let {
                    for (document in it) {
                        document.data?.let { data ->
                               val qrCodeModel =  QrCodeModel.fromSnapshot(
                                    document.id,
                                    data
                                )
                                val bundle = Bundle()
                                bundle.putString("museuId", qrCodeModel.museuId)
                                bundle.putString("obraId", qrCodeModel.obraId)
                                findNavController().navigate(R.id.obrasExplore, bundle)
                            val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
                            navBar.menu.findItem(R.id.home).isChecked = true
                        }
                    }
                }
            }
    }
}
