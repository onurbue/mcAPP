package models

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
import com.google.firebase.firestore.firestore

@Entity
data class Eventos(
    @PrimaryKey
    @ColumnInfo(name = "evento_id")
    var id: String,
    @ColumnInfo(name = "evento_name") var name: String,
    @ColumnInfo(name = "evento_image") var image: String,
    @ColumnInfo(name = "evento_date") var date: String,
    @ColumnInfo(name = "evento_description") var description: String,
    @ColumnInfo(name = "evento_img_description")  var imgDescription: String?,
    @ColumnInfo(name = "museu_id") var museuId: String? = null
) {
    companion object {
        fun fromSnapshot(id: String, snapshot: Map<String, Any>): Eventos {
            return Eventos(
                id,
                snapshot.get("name") as String,
                snapshot.get("pathToImg") as String ,
                snapshot.get("data") as String ,
                snapshot.get("description") as String,
                snapshot.get("imgDescription") as String?
            )
        }

        fun fetchEventos(museuId : String?, callback: (List<Eventos>) -> Unit){

            val db = Firebase.firestore

            db.collection("museus").document(museuId!!).collection("eventos")
                .addSnapshotListener { snapshoot, error ->
                    snapshoot?.documents?.let {
                        var eventos = arrayListOf<Eventos>()
                        eventos.clear()
                        for (document in it) {
                            document.data?.let { data ->
                                eventos.add(
                                    fromSnapshot(
                                        document.id,
                                        data
                                    )
                                )
                            }
                        }
                        callback(eventos)
                    }
                }
        }
    }


}
