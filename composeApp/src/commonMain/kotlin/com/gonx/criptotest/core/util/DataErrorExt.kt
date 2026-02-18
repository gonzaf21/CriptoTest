package com.gonx.criptotest.core.util

import com.gonx.criptotest.core.domain.DataError
import criptotest.composeapp.generated.resources.Res
import criptotest.composeapp.generated.resources.error_disk_full
import criptotest.composeapp.generated.resources.error_insufficient_balance
import criptotest.composeapp.generated.resources.error_no_internet
import criptotest.composeapp.generated.resources.error_request_timeout
import criptotest.composeapp.generated.resources.error_serialization
import criptotest.composeapp.generated.resources.error_too_many_requests
import criptotest.composeapp.generated.resources.error_unknown
import org.jetbrains.compose.resources.StringResource

fun DataError.uiText(): StringResource {
    return when(this) {
        DataError.Remote.REQUEST_TIMEOUT -> Res.string.error_request_timeout
        DataError.Remote.TOO_MANY_REQUESTS -> Res.string.error_too_many_requests
        DataError.Remote.NO_INTERNET -> Res.string.error_no_internet
        DataError.Remote.SERVER -> Res.string.error_unknown
        DataError.Remote.SERIALIZATION -> Res.string.error_serialization
        DataError.Remote.UNKNOWN -> Res.string.error_unknown
        DataError.Local.DISK_FULL -> Res.string.error_disk_full
        DataError.Local.INSUFFICIENT_FUNDS -> Res.string.error_insufficient_balance
        DataError.Local.UNKNOWN_ERROR -> Res.string.error_unknown
    }
}