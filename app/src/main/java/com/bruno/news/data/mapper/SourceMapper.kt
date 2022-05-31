package com.bruno.news.data.mapper

import com.bruno.news.data.response.SourceResponse
import com.bruno.news.domain.model.Source

object SourceMapper {

    fun SourceResponse?.toSource() = Source(
        id = this?.id.orEmpty(),
        name = this?.name.orEmpty()
    )
}