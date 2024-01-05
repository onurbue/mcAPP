package models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.projetocma.R

class Obras(
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
    }
}
