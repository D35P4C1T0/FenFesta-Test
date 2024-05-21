package com.example.logintest.view


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.logintest.data.location.LocationService
import com.example.logintest.view.components.MBMap
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import kotlinx.coroutines.delay

@OptIn(MapboxExperimental::class)
@Composable
fun MapScreen(modifier: Modifier = Modifier) {

    var firstStart by remember {
        mutableStateOf(true)
    }

    var point: Point? by remember {
        mutableStateOf(null)
    }
    var relaunch by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current


    val permissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (!permissions.values.all { it }) {
                //handle permission denied
                println("Permission denied")
            } else {
                relaunch = !relaunch
            }
        }
    )

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            center(Point.fromLngLat(-73.977001, 40.728847))
            zoom(1.0)
            pitch(0.0)
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        MBMap(
            onPointChange = { point = it },
            mapViewportState = mapViewportState,
            point = point,
            modifier = Modifier
                .fillMaxSize()

        )
    }

    LaunchedEffect(key1 = relaunch) {

        if (firstStart) {
            delay(500)
            mapViewportState.flyTo(
                cameraOptions = cameraOptions {
                    center(Point.fromLngLat(11.05, 46.12))
                    zoom(10.0)
                },
                animationOptions = MapAnimationOptions.mapAnimationOptions { duration(5000) },
            )
            firstStart = false
        }

        try {
            val location = LocationService().getCurrentLocation(context)

        } catch (e: LocationService.LocationServiceException) {
            when (e) {
                is LocationService.LocationServiceException.LocationDisabledException -> {
                    //handle location disabled, show dialog or a snack-bar to enable location
                }

                is LocationService.LocationServiceException.MissingPermissionException -> {
                    permissionRequest.launch(
                        arrayOf(
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }

                is LocationService.LocationServiceException.NoNetworkEnabledException -> {
                    //handle no network enabled, show dialog or a snack-bar to enable network
                }

                is LocationService.LocationServiceException.UnknownException -> {
                    //handle unknown exception
                }
            }
        }
    }
}