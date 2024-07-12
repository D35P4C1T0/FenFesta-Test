package com.example.logintest.ui.theme.screens


import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.logintest.data.location.LocationService
import com.example.logintest.data.viewmodel.EventViewModel
import com.example.logintest.model.EventModel
import com.example.logintest.view.components.Annotations
import com.example.logintest.view.utils.FirstLaunch
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.extension.localization.localizeLabels
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.generated.LocationComponentSettings
import com.mapbox.maps.plugin.locationcomponent.location
import java.util.Locale

@OptIn(MapboxExperimental::class)
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    mapViewportState: MapViewportState,
    eventsList: List<EventModel>,
    updateEvents: () -> Unit,
    isFirstLaunch: FirstLaunch,
    onMarkerClick: (String) -> Unit,
) {

//    Log.d("Compose", "Map is recomposing")

    var relaunch by remember {
        mutableStateOf(false)
    }

    val permissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (!permissions.values.all { it }) {
                //handle permission denied
                println("Permission denied")
            } else {
                relaunch = !relaunch
                println("Permission granted")
            }
        }
    )

    //AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        val context = LocalContext.current
        MapboxMap(
            Modifier.fillMaxSize(),
            mapViewportState = mapViewportState,
            style = { MapStyle(style = Style.MAPBOX_STREETS) },
            scaleBar = { }, // no scale bar
        )
        {
            LaunchedEffect(key1 = isFirstLaunch) {
//                viewModel.fetchEvents()
            }

            Annotations(eventList = eventsList, onClick = onMarkerClick)
            MapEffect(Unit) { mapView ->
                // Use mapView to access all the Mapbox Maps APIs including plugins etc.
                // For example, to enable debug mode:
                mapView.mapboxMap.style?.localizeLabels(locale = Locale("it"))
                mapView.location.pulsingEnabled = true
                mapView.location.apply {
                    enabled = true
                    locationPuck = createDefault2DPuck(withBearing = true)
                    puckBearingEnabled = true
                    puckBearing = PuckBearing.HEADING
                }
            }
        }

        LaunchedEffect(key1 = relaunch) {
            try {
                updateEvents()
                println("map relaunch called")
                val location = LocationService().getCurrentLocation(context)
                println("Location: $location")
                if (isFirstLaunch.isFirstLaunch) { // working on first time launch
                    mapViewportState.flyTo(
                        cameraOptions = CameraOptions.Builder()
                            .center(location)
                            .zoom(15.0)
                            .build(),
                        animationOptions = MapAnimationOptions.mapAnimationOptions {
                            duration(
                                3500
                            )
                        },
                    )
                    isFirstLaunch.toggleFirstLaunch()
                }

            } catch (e: LocationService.LocationServiceException) {
                when (e) {
                    is LocationService.LocationServiceException.LocationDisabledException -> {
                        //handle location disabled, show dialog or a snack-bar to enable location
                        println("Location disabled")
                    }

                    is LocationService.LocationServiceException.MissingPermissionException -> {
                        permissionRequest.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }

                    is LocationService.LocationServiceException.NoNetworkEnabledException -> {
                        //handle no network enabled, show dialog or a snack-bar to enable network
                        println("No network enabled")
                    }

                    is LocationService.LocationServiceException.UnknownException -> {
                        //handle unknown exception
                        println("Unknown exception")
                    }
                }
            }
        }
    }
}
//}