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

data class Obras(
    @PrimaryKey
    @ColumnInfo(name ="id_obra")
    var id: String = "",
    @ColumnInfo(name ="name_obra") var name: String ,
    @ColumnInfo(name = "image_obra") var image: String? ,
    @ColumnInfo(name ="description_obra")var description: String,
    @ColumnInfo(name ="img_description_obra")var imgDescription: String



){
    constructor(): this("", "","","","")
    companion object{
        fun fromSnapshot(id : String, snapshot: Map<String,Any>) : Obras{
            return Obras(id,
                snapshot.get("name") as String,
                snapshot.get("pathToImg") as? String?,
                snapshot.get("description") as String,
                snapshot.get("imgDescription") as String
            )
        }

         fun fetchObras(museuId: String?, callback: (List<Obras>)  -> Unit){

            val db = Firebase.firestore

            db.collection("museus").document(museuId!!)
                .collection("obras")
                .addSnapshotListener { snapshoot, error ->
                    snapshoot?.documents?.let {
                        val obras = arrayListOf<Obras>()
                        obras.clear()
                        for (document in it) {
                            document.data?.let { data ->
                                obras.add(
                                    fromSnapshot(
                                        document.id,
                                        data
                                    )
                                )
                            }
                        }
                        callback(obras)
                    }

                }

        }

        fun fetchObra(museuId: String?, obraId: String?, callback: (Obras) -> Unit){

            val db = Firebase.firestore

            val obraRef = db.collection("museus")
                .document(museuId!!)
                .collection("obras")
                .document(obraId!!)

            obraRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle the error
                    return@addSnapshotListener
                }
                    // Parse the snapshot data into your Obras class
                val obra = fromSnapshot(obraId, snapshot?.data ?: emptyMap())

                    callback(obra)
            }

        }
    }

}

