package com.oam.weatherapp.util

import kotlinx.coroutines.flow.*

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,          // Load from DB
    crossinline fetch: suspend () -> RequestType,       // Call API
    crossinline saveFetchResult: suspend (RequestType) -> Unit, // Save API response to DB
    crossinline shouldFetch: (ResultType?) -> Boolean = { true } // Decide when to fetch
): Flow<Resource<ResultType>> = flow {
    emit(Resource.Loading())
    val data = query().firstOrNull()

    if (shouldFetch(data)) {
        try {
            val apiResponse = fetch()
            saveFetchResult(apiResponse)
            emitAll(query().map { Resource.Success(it) })
        } catch (throwable: Throwable) {
            emitAll(query().map { Resource.Error(throwable.message ?: "Unknown error", it) })
        }
    } else {
        emitAll(query().map { Resource.Success(it) })
    }
}
