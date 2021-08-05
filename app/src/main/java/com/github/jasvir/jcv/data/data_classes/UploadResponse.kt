package com.github.jasvir.jcv.data.data_classes

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
data class UploadResponse(
    val result: Result,
    @Json(name = "FileId")
    val fileId: String? = "",
    @Json(name = "FileName")
    val fileName: String? = ""
)

@JsonClass(generateAdapter = true)
@Parcelize
data class Result(val faces: List<Faces>):Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Faces(val coordinates: Coordinates):Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Coordinates(
    val height: Int,
    val width: Int,
    val xmax: Int,
    val xmin: Int,
    val ymax: Int,
    val ymin: Int
):Parcelable