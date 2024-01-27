package models

data class TicketsComprados (
    var id: String,
    var userId : String?,
    var date : String?,
    var name: String?,
    var pathToImg: String?,
    var price: String?,
    var description: String?,
){
    fun toHashMap(): Map<String, Any?> {
        return hashMapOf(
            "date" to date,
            "userId" to userId,
            "name" to name,
            "pathToImg" to pathToImg,
            "description" to description,
            "price" to price,
        )
    }

    companion object {
        fun fromSnapshot(id: String, snapshot: Map<String, Any>): TicketsComprados {
            return TicketsComprados(
                id,
                snapshot["userId"] as String ,
                snapshot["date"] as String,
                snapshot["name"] as String,
                snapshot["pathToImg"] as? String?,
                snapshot["price"] as String ,
                snapshot["description"] as String


            )
        }
    }
}