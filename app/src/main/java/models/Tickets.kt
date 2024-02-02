package models;

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.projetocma.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

@Entity
data class Tickets(
    @PrimaryKey
    @ColumnInfo(name = "id_ticket") var id: String,
    @ColumnInfo(name = "name_ticket")var name: String,
    @ColumnInfo(name = "img_ticket")var pathToImg: String?,
    @ColumnInfo(name = "price_ticket")var price: String,
    @ColumnInfo(name = "description_ticket")var description: String,
    @ColumnInfo(name = "quantity_bought_ticket")var quantityBought: Long
) {
    fun toHasMap(): Map<String, Any?> {
        return hashMapOf(
            "name" to name,
            "pathToImg" to pathToImg,
            "description" to description,
            "price" to price,
        )
    }

    companion object {
        fun fromSnapshot(id: String, snapshot: Map<String, Any>): Tickets {
            return Tickets(
                id,
                snapshot["name"] as String,
                snapshot["pathToImg"] as? String?,
                snapshot["price"] as String ,
                snapshot["description"] as String,
                snapshot["quantityBought"] as Long


                )
        }

        fun fetchTickets(museuId : String, callback: (List<Tickets>) -> Unit){

            val db = Firebase.firestore

            db.collection("museus").document(museuId).collection("tickets")
                .addSnapshotListener { snapshoot, error ->
                    snapshoot?.documents?.let {
                        val tickets = arrayListOf<Tickets>()
                        tickets.clear()
                        for (document in it) {
                            document.data?.let { data ->
                                tickets.add(
                                    Tickets.fromSnapshot(
                                        document.id,
                                        data
                                    )
                                )
                            }
                        }
                        callback(tickets)
                    }
                }
        }

        fun addTicket( ticketMap: Map<String, Any?>){
            val db = Firebase.firestore

            db.collection("bilhetesUser")
                .add(ticketMap)
                .addOnSuccessListener { documentReference ->
                    val ticketId = documentReference.id
                }
                .addOnFailureListener { e ->
                }
        }

        fun updateTicketBought(museuId: String, ticketId: String){
            val db = Firebase.firestore

            db.collection("museus").document(museuId!!).collection("tickets").document(ticketId!!)
                .update("quantityBought", FieldValue.increment(1))
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "Quantity updated successfully!")
                }
                .addOnFailureListener { e ->
                }
        }
    }
}
