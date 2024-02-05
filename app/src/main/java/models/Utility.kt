package models

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView

abstract class Utility {

    companion object {

        fun setImage(pathToImage: String?, imageView: ImageView, context: Context) {
            pathToImage?.let { imagePath ->
                // Load the image from Firebase Storage
                val storage = Firebase.storage
                val storageRef = storage.reference
                val pathReference = storageRef.child(imagePath)
                pathReference.downloadUrl.addOnSuccessListener { uri ->
                    Glide.with(context)
                        .load(uri)
                        .into(imageView)
                }.addOnFailureListener {
                }
            }
        }

         fun configMap(latitude: Double?, longitude: Double?, mapView: MapView) {

            mapView.mapboxMap.setCamera(
                CameraOptions.Builder()
                    .center(com.mapbox.geojson.Point.fromLngLat(longitude!!, latitude!!))
                    .pitch(3.0)
                    .zoom(15.0)
                    .bearing(0.0)
                    .build()
            )
        }

         fun isValidEmail(email: String): Boolean {
            return email.matches(Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}"))
        }

         fun isValidUsername(username: String): Boolean {
            return username.length in 6..16
        }

         fun isValidPhoneNumber(phoneNumber: String): Boolean {
            return phoneNumber.length == 9
        }
    }
}