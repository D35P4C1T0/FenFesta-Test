package com.example.logintest.view.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.example.logintest.model.EventModel
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotationGroup
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions

@OptIn(MapboxExperimental::class)
@Composable
fun Annotations(modifier: Modifier = Modifier, eventList: List<EventModel>) {
    println("Adding annotations to the map... $eventList")
    val listOfAnnotations = toAnnotations(eventList)
    val context = LocalContext.current
    CircleAnnotationGroup(annotations = listOfAnnotations)
}

private fun toAnnotations(eventList: List<EventModel>): List<CircleAnnotationOptions> {
    return eventList.map {
        CircleAnnotationOptions()
            .withPoint(Point.fromLngLat(it.lon.toDouble(), it.lat.toDouble()))
            .withCircleColor(Color.Blue.toArgb())
            .withCircleRadius(10.0)
    }
}