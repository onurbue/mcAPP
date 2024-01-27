package models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.projetocma.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

data class Obras(
    var id: String?,
    var name: String,
    var image: String?,
    var description: String,
    var imgDescription: String
){
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
                                    Obras.fromSnapshot(
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
    }
}
