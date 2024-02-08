import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projetocma.R
import com.example.projetocma.databinding.FragmentMbwayBinding

class MBWay : Fragment() {

    private lateinit var binding: FragmentMbwayBinding
    private var totalTimeInMillis = 180000L
    private var timeLeftInMillis = totalTimeInMillis
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMbwayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val timeTextView = binding.timeText
        startTimer(timeTextView)
    }

    private fun startTimer(timeTextView: TextView) {
        runnable = object : Runnable {
            override fun run() {
                val seconds = (timeLeftInMillis / 1000).toInt()
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                val timeLeftFormatted = String.format("%02d:%02d", minutes, remainingSeconds)

                timeTextView.post {
                    timeTextView.text = timeLeftFormatted
                }

                if (timeLeftInMillis > 0) {
                    timeLeftInMillis -= 1000
                    if (timeLeftInMillis == totalTimeInMillis - 10000) {

                // MUDA DE PAGINA QUANDO ACABA 10 SEGUNDOS PARA MOSTRAR

               } else {

                        handler.postDelayed(this, 1000)
                    }
                } else {
                    findNavController().popBackStack()
                }
            }
        }
        handler.post(runnable)
    }


    override fun onDestroyView() {
        handler.removeCallbacks(runnable)
        super.onDestroyView()
    }
}
