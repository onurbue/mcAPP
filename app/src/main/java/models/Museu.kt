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
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import org.checkerframework.checker.nullness.qual.NonNull


@Entity
data class Museu(
    @PrimaryKey
    @ColumnInfo(name = "id_museu")
    var id: String ,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "path_to_image") var image: String?,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "categoria")var categoria: String?,
    @ColumnInfo(name = "latitude")var latitude : Double,
    @ColumnInfo(name = "longitude")var longitude: Double,
    @ColumnInfo(name = "quantity_clicked")var quantityClicked: Long



){


    companion object{
        fun fromSnapshot(id : String, snapshot: Map<String,Any>) : Museu{
            return Museu(id,
                snapshot.get("name") as String,
                snapshot.get("pathToImg") as? String,
                snapshot.get("description") as String,
                snapshot["categoria"] as? String,
                snapshot.get("latitude") as Double,
                snapshot.get("longitude") as Double,
                snapshot.get("quantityClicked") as Long
            )
        }
        fun fetchMuseums(onCompletion: (List<Museu>) -> Unit) {
            val db = FirebaseFirestore.getInstance()

            db.collection("museus")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val museums = ArrayList<Museu>()

                    for (document in querySnapshot.documents) {
                        document.data?.let { data ->
                            museums.add(
                                fromSnapshot(
                                    document.id,
                                    data
                                )
                            )
                        }
                    }
                    onCompletion(museums)

                }
        }

        fun getMuseumsByCategory(category: String, callback: (List<Museu>) -> Unit) {
            val db = Firebase.firestore

            db.collection("museus")
                .whereEqualTo("categoria", category)
                .addSnapshotListener { snapshot, error ->
                    snapshot?.documents?.let {
                        val museums = mutableListOf<Museu>()
                        for (document in it) {
                            document.data?.let { data ->
                                museums.add(
                                    fromSnapshot(
                                        document.id,
                                        data
                                    )
                                )
                            }
                        }
                        callback(museums)
                    }
                }
        }

        fun updateQuantityClicked(museuId: String){
            val db = Firebase.firestore

            db.collection("museus").document(museuId)
                .update("quantityClicked", FieldValue.increment(1))
                .addOnSuccessListener { documentReference ->
                    Log.d("quantity", "Quantity updated successfully!")
                }
                .addOnFailureListener { e ->
                }
        }


    }


}
