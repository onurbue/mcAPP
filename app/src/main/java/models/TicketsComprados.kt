package models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Entity
data class TicketsComprados (
    @PrimaryKey
    @ColumnInfo(name = "id_ticket_comprado") var id: String,
    @ColumnInfo(name = "userId_ticket_comprado")var userId : String?,
    @ColumnInfo(name = "date_ticket_comprado")var date : String?,
    @ColumnInfo(name = "name_ticket_comprado")var name: String?,
    @ColumnInfo(name = "img_ticket_comprado")var pathToImg: String?,
    @ColumnInfo(name = "price_ticket_comprado")var price: String?,
    @ColumnInfo(name = "description_ticket_comprado")var description: String?,
){
    fun toHashMap(): Map<String, Any?> {
        return hashMapOf(
            "date" to date,
            "userId" to userId,
            "name" to name,
            "pathToImg" to pathToImg,
            "description" to description,
            "price" to price,
        )
    }

    companion object {
        fun fromSnapshot(id: String, snapshot: Map<String, Any>): TicketsComprados {
            return TicketsComprados(
                id,
                snapshot["userId"] as String ,
                snapshot["date"] as String,
                snapshot["name"] as String,
                snapshot["pathToImg"] as? String?,
                snapshot["price"] as String ,
                snapshot["description"] as String


            )
        }

        fun getTicketsComprados(userId: String?, callback: (List<TicketsComprados>) -> Unit){
            val db = Firebase.firestore

            db.collection("bilhetesUser").whereEqualTo("userId", userId)
                .addSnapshotListener { snapshoot, error ->
                    snapshoot?.documents?.let {
                        var ticketsComprados = arrayListOf<TicketsComprados>()
                        ticketsComprados.clear()
                        for (document in it) {
                            document.data?.let { data ->
                                ticketsComprados.add(
                                    fromSnapshot(
                                        document.id,
                                        data
                                    )
                                )
                            }
                        }
                        callback(ticketsComprados)
                    }
                }
        }
    }
}