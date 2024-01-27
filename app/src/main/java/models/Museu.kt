package models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.projetocma.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


data class Museu(
 var id: String?,
    var name: String,
    var image: String?,
    var description: String,
    var categoria: String?,
    var latitude : Double,
    var longitude: Double,
    var quantityClicked: Long



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
                                Museu.fromSnapshot(
                                    document.id,
                                    data
                                )
                            )
                        }
                    }

                    onCompletion(museums)
                }
                .addOnFailureListener { exception ->
                    // Handle errors
                    // You might want to provide error information to the caller
                    onCompletion(ArrayList())  // Passing an empty list in case of failure
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
                                    Museu.fromSnapshot(
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


    }


}
