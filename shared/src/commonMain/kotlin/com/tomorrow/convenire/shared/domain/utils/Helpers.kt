package com.tomorrow.convenire.shared.domain.utils

import io.ktor.http.URLBuilder
import io.ktor.http.Url

class ThumbnailUrlHelper {

    // the image url can be altered to smart crop the image, based on the dimensions provided as path segment
    // the image handler we are using accepts multiple dimensions but will apply the first dimension it finds,
    // which is why I am adding the required dimensions as the 1st path segment. It will ignore any other dimensions already present in the URL
    // this will fail if we stop using the serverless image handler or move to another one
    fun setImageShapeInUrl(shape: ImageShape, urlString: String): String {
        if (urlString == "") return ""

        val url = Url(urlString)

        return addPathSegment(
            baseUrl = url,
            pathSegment = when(shape) {
                ImageShape.Square -> "200x200"
                ImageShape.Rectangle -> "1000x562"
            },
            index = 0
        ).toString()
    }

    private fun addPathSegment(baseUrl: Url, pathSegment: String, index: Int): Url {
        val segments = baseUrl.pathSegments.toMutableList()
        segments.add(index, pathSegment)

        val newUrl = URLBuilder(baseUrl).apply {
            pathSegments = segments
        }.build()

        return newUrl
    }
}

enum class ImageShape {
    Square,
    Rectangle
}