package com.example.projetocma

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.projetocma.databinding.FragmentQrCodeBinding
import com.example.projetocma.museu.MuseusExplore
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.BarcodeView

class QrCode : Fragment() {
    private lateinit var binding: FragmentQrCodeBinding
    //url para teste
    private val targetURL = "https://qrco.de/beb0YR"
    private lateinit var barcodeView: BarcodeView
    private val MY_PERMISSIONS_CAMERA = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentQrCodeBinding.inflate(inflater,container,false)
        // Infla o layout do fragmento
        val view = inflater.inflate(R.layout.fragment_qr_code, container, false)

        // Encontra o BarcodeView no layout inflado
        barcodeView = view.findViewById(R.id.barcode_scanner)

        // Verifica se a permissão da câmera foi concedida
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Solicita permissão da câmera se ainda não foi concedida
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                MY_PERMISSIONS_CAMERA
            )
        } else {
            // Inicia a detecção contínua do código de barras se a permissão foi concedida
            startScanning()
        }

        binding.setaBackk.setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

    private fun startScanning() {
        // Configura o BarcodeView para decodificar códigos de barras continuamente
        barcodeView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                result?.let {
                    // Verifica se o texto do QR code é o URL desejado
                    if (it.text == targetURL) {
                        // Se for o URL desejado, direcione para a próxima página
                        val intent = Intent(requireContext(), MuseusExplore::class.java)
                        startActivity(intent)
                    } else {
                        // Se não for o URL desejado, exibe uma mensagem ao usuário
                        Toast.makeText(
                            requireContext(),
                            "QR code escaneado não corresponde ao URL desejado.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
                // Lógica para pontos de resultado possíveis, se necessário
            }
        })
    }

    override fun onResume() {
        super.onResume()
        // Resume a detecção do código de barras quando o fragmento é retomado
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
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
}