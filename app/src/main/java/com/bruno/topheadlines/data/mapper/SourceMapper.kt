package com.bruno.topheadlines.data.mapper

import com.bruno.topheadlines.data.response.SourceResponse
import com.bruno.topheadlines.domain.model.Source

object SourceMapper {

    fun SourceResponse?.toSource() = Source(
        id = this?.id.orEmpty(),
        name = this?.name.orEmpty()
    )
}