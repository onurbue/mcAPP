package models

data class QrCodeModel (
    var id: String,
    var qrCodeString : String,
    var museuId : String,
    var obraId : String,
){
    companion object{
        fun fromSnapshot(id : String, snapshot: Map<String,Any>) : QrCodeModel{
            return QrCodeModel(id,
                snapshot.get("qrCodeString") as String,
                snapshot.get("museuId") as String,
                snapshot.get("obraId") as String
                )
        }
    }
}