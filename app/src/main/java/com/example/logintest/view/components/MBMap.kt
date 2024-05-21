package com.example.logintest.view.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.extension.localization.localizeLabels
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.generated.LocationComponentSettings
import com.mapbox.maps.plugin.locationcomponent.location
import java.util.Locale

@OptIn(MapboxExperimental::class)
@Composable
fun MBMap(
    onPointChange: (Point) -> Unit,
    point: Point?,
    mapViewportState: MapViewportState,
    modifier: Modifier
) {
    MapboxMap(
        Modifier.fillMaxSize(),
        locationComponentSettings = LocationComponentSettings
            .Builder(
                createDefault2DPuck(withBearing = true)
            )
            .setEnabled(true)
            .setPuckBearingEnabled(true)
            .setPuckBearing(PuckBearing.HEADING)
            .build(),
        mapViewportState = mapViewportState,
        style = {
            MapStyle(style = Style.STANDARD)
        }

    )
    {
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
}

