package models

data class User (
    val id: String,
    val username: String,
    val email: String,
    val phoneNumber:String
){
    fun toHashMap(): Map<String, Any?> {
        return hashMapOf(
            "name" to username,
            "email" to email,
            "phoneNumber" to phoneNumber,
        )
    }

    companion object {
        fun fromSnapshot(id: String, snapshot: Map<String, Any>): User {
            return User(
                 id,
                snapshot["name"] as String,
                snapshot["email"] as String,
                snapshot["phoneNumber"] as String

            )
        }
    }
}