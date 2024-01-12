package models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.projetocma.R

class Museu(
    var id: String?,
    var name: String,
    var image: String?,
    var description: String,
    var categoria: String?,



){
    fun toHasMap() : Map<String, Any?>{
        return hashMapOf(
            "name" to name,
            "pathToImg" to image,
            "description" to description,
            "categoria" to categoria
        )
    }

    companion object{
        fun fromSnapshot(id : String, snapshot: Map<String,Any>) : Museu{
            return Museu(id,
                snapshot.get("name") as String,
                snapshot.get("pathToImg") as? String,
                snapshot.get("description") as String,
                snapshot["categoria"] as? String
            )
        }
    }
}
