package com.example.logintest.ui.theme.screens.minimap

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import com.example.logintest.R
import com.example.logintest.data.viewmodel.LocationViewModel
import com.example.logintest.model.LocationModel
import com.example.logintest.view.components.getBitmapFromVectorDrawable
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.extension.localization.localizeLabels
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import java.util.Locale

@OptIn(MapboxExperimental::class)
@Composable
fun MiniMap(
    modifier: Modifier,
    mapViewportState: MapViewportState,
    locationData: LocationModel,
) {

    val context = LocalContext.current
//    val locationData by locationViewModel.locationData.collectAsState()

    AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
        Box(
            modifier = modifier
                .clip(shape = MaterialTheme.shapes.medium)
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
//            val context = LocalContext.current
            MapboxMap(
                Modifier.fillMaxSize(),
                mapViewportState = mapViewportState,
                style = {
                    MapStyle(style = Style.MAPBOX_STREETS)
                }
            )
            {
                locationData?.let {
                    PointAnnotation(
                        point = Point.fromLngLat(
                            locationData!!.lon,
                            locationData!!.lat
                        ),
                        iconImageBitmap = getBitmapFromVectorDrawable(
                            context,
                            R.drawable.marker_icon
                        ),
                        iconSize = 2.35,
                    )
                }

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
                    MapUtils.centerToLocation(
                        mapView,
                        Point.fromLngLat(locationData!!.lon, locationData!!.lat)
                    )
                }
            }
        }
    }
}

object MapUtils {
    fun centerToLocation(mapView: MapView, point: Point) {
        mapView.camera.flyTo(
            CameraOptions.Builder()
                .center(point)
                .zoom(16.5)
                .build()
        )
        mapView.mapboxMap.setCamera(CameraOptions.Builder().center(point).build())
    }
}