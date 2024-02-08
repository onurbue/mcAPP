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
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import models.Tickets
import models.TicketsComprados
import models.Utility
import java.util.Date
import java.util.UUID

class MBWay : Fragment() {

    private lateinit var binding: FragmentMbwayBinding
    private var totalTimeInMillis = 180000L
    private var timeLeftInMillis = totalTimeInMillis
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    val randomUid: String = UUID.randomUUID().toString()
    var userId : String? = null
    var selectedDate: String? = null
    var name : String? = null
    var description : String? = null
    var price : String? = null
    var pathToImage : String? = null
    var museuId : String? = null
    var ticketId : String? = null
    var phoneNumber : String? = null
    var quantity: Int? = null
    var finalPrice : Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectedDate = arguments?.getString("formattedDate")
        userId = arguments?.getString("userId")
        name = arguments?.getString("name")
        description = arguments?.getString("description")
        price = arguments?.getString("price")
        pathToImage = arguments?.getString("pathToImage")
        museuId = arguments?.getString("museuId")
        ticketId = arguments?.getString("ticketId")
        quantity = arguments?.getInt("quantity")
        finalPrice = arguments?.getInt("finalPrice")
        phoneNumber = arguments?.getString("phoneNumber")

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMbwayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mbwayTotalValue.text = "$finalPrice €"
        binding.mbwayPhone.text = phoneNumber
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

                        for (i in 0 until quantity!!) {

                            val ticketComprado = TicketsComprados(
                                id = randomUid,
                                date = selectedDate,
                                userId = userId,
                                name = name,
                                pathToImg = pathToImage,
                                description = description,
                                price = price
                            )
                            val ticketMap = ticketComprado.toHashMap()

                            Tickets.addTicket(ticketMap)

                            Tickets.updateTicketBought(museuId!!, ticketId!!)
                        }


                findNavController().navigate(R.id.action_MBWay3_to_museusPageFrag)
                Utility.showCustomToast(requireContext(),"O pagamento foi bem sucedido")

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
