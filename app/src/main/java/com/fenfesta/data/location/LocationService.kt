package com.fenfesta.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.mapbox.geojson.Point
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class LocationService {
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(context: Context): Point {
        if (!context.hasPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            throw LocationServiceException.MissingPermissionException()
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        // TODO: ^^^ investiga l'origine di questo errore (NO SIM)

        if (!isGpsEnabled) {
            throw LocationServiceException.LocationDisabledException()
        }
//        if (!isNetworkEnabled) {
//            throw LocationServiceException.NoNetworkEnabledException()
//        }

        val locationProvider = LocationServices.getFusedLocationProviderClient(context)
        val request = CurrentLocationRequest.Builder()
//            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        return suspendCancellableCoroutine { continuation ->
            locationProvider.getCurrentLocation(request, null)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        continuation.resume(Point.fromLngLat(location.longitude, location.latitude))
                    } else {
                        continuation.resumeWithException(
                            LocationServiceException.UnknownException(
                                Exception("Location is null")
                            )
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(
                        LocationServiceException.UnknownException(
                            exception
                        )
                    )
                }
                .addOnCanceledListener {
                    continuation.resumeWithException(
                        LocationServiceException.UnknownException(
                            Exception("Location request was canceled")
                        )
                    )
                }

            continuation.invokeOnCancellation {
                // Optionally handle coroutine cancellation here
            }
        }
    }

    private fun Context.hasPermissions(vararg permissions: String) =
        permissions.all {
            ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }


    sealed class LocationServiceException : Exception() {
        class MissingPermissionException : LocationServiceException()
        class LocationDisabledException : LocationServiceException()
        class NoNetworkEnabledException : LocationServiceException()
        class UnknownException(val exception: Exception) : LocationServiceException()
    }

}