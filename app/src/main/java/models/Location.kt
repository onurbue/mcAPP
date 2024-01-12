package models

class Location (
    var id: String,
    var latitude : Double,
    var longitude: Double,
){
    companion object{
        fun fromSnapshot(id : String, snapshot: Map<String,Any>) : Location{
            return Location(id,
                snapshot.get("latitude") as Double,
                snapshot.get("longitude") as Double,

            )
        }
    }
}