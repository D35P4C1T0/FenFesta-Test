package com.example.logintest.view.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.core.content.ContextCompat
import com.example.logintest.R
import com.example.logintest.model.EventModel
import com.google.gson.JsonObject
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotationGroup
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotationGroup
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.json.JSONObject


@OptIn(MapboxExperimental::class)
@Composable
fun Annotations(modifier: Modifier = Modifier, eventList: List<EventModel>) {
    println("Adding annotations to the map... $eventList")
    val context = LocalContext.current
    val bitmap = getBitmapFromVectorDrawable(context, R.drawable.marker_icon)
//    val listOfAnnotations = toAnnotations(eventList)
    val listOfAnnotations = toAnnotations(eventList, bitmap)
    PointAnnotationGroup(
        annotations = listOfAnnotations,
        onClick = { pointAnnotation ->
            println("Annotation clicked: ${pointAnnotation.getData()?.asJsonObject?.get("id")}")
            true
        })
}

//private fun toAnnotations(eventList: List<EventModel>): List<CircleAnnotationOptions> {
//    return eventList.map {
//        CircleAnnotationOptions()
//            .withPoint(Point.fromLngLat(it.lon.toDouble(), it.lat.toDouble()))
//            .withCircleColor(Color.Blue.toArgb())
//            .withCircleRadius(10.0)
//    }
//}

private fun toAnnotations(eventList: List<EventModel>, icon : Bitmap): List<PointAnnotationOptions> {
    return eventList.map { event ->
        PointAnnotationOptions()
            .withPoint(Point.fromLngLat(event.lon.toDouble(), event.lat.toDouble()))
            .withIconImage(icon)
            .withIconSize(2.35)
            .withData(JsonObject().apply {
                addProperty("id", event.id)
                addProperty("title", event.name)
            })
    }
}

private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(context, drawableId)
    val bitmap = Bitmap.createBitmap(
        drawable!!.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}