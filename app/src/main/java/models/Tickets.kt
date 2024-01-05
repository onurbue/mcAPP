package models;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.projetocma.R


class Tickets(
    var id: String,
    var name: String,
    var pathToImg: String?,
    var price: String,
    var description: String,
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
                snapshot["description"] as String


                )
        }
    }
}
