package com.accu.cityweather.location

import android.app.Activity
import android.content.Context
import android.location.Location
import com.accu.cityweather.location.LocationProvider.LocationResult.FatalError
import com.accu.cityweather.location.LocationProvider.LocationResult.LocationSettingsOff
import com.accu.cityweather.location.LocationProvider.LocationResult.PermissionDenied
import com.accu.cityweather.location.LocationProvider.LocationResult.Success
import com.birjuvachhani.locus.Locus
import com.birjuvachhani.locus.LocusResult
import com.birjuvachhani.locus.isDenied
import com.birjuvachhani.locus.isPermanentlyDenied
import com.birjuvachhani.locus.isSettingsDenied
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface LocationProvider {
    suspend fun getCurrentLocation(context: Context): LocationResult {
        return suspendCancellableCoroutine { continuation ->
            Locus.getCurrentLocation(context) { result: LocusResult ->
                result.location?.let {
                    continuation.resume(Success(it))
                }
                result.error?.let {
                    val errorResult = when {
                        it.isPermanentlyDenied || it.isDenied -> PermissionDenied
                        it.isSettingsDenied -> LocationSettingsOff
                        else -> FatalError
                    }
                    continuation.resume(errorResult)
                }
            }
            continuation.invokeOnCancellation { Locus.stopLocationUpdates() }
        }
    }

    sealed class LocationResult {
        data class Success(val location: Location) : LocationResult()
        object PermissionDenied : LocationResult()
        object LocationSettingsOff : LocationResult()
        object FatalError : LocationResult()
    }
}
