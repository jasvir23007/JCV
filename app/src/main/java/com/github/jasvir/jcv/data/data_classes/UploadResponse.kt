package com.github.jasvir.jcv.data.data_classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UploadResponse(
    @Json(name = "result")
    val result: Result,
    @Json(name = "FileId")
    val fileId: String? = "",
    @Json(name = "FileName")
    val fileName: String? = ""
)

data class Result(val faces:Faces)
data class Faces(val coordinates:Coordinates)

data class Coordinates(val height:Int,val width:Int,val xmax:Int,val xmin:Int,val ymax:Int,val ymin:Int)