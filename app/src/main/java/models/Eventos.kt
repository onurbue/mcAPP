package models

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.projetocma.R

class Eventos(
    var id: String?,
    var name: String,
    var image: String,
    var date: String,
    var description: String,
    var imgDescription: String?
) {
    companion object {
        fun fromSnapshot(id: String, snapshot: Map<String, Any>): Eventos {
            return Eventos(
                id,
                snapshot["name"] as String,
                snapshot["pathToImg"] as String ,
                snapshot["data"] as String ,
                snapshot["description"] as String,
                snapshot["imgDescription"] as? String?
            )
        }
    }
}
