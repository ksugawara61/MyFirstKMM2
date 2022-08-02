package net.listadoko.myfirstkmm2.repository.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object ApiClient {
    val client = HttpClient() {
        install(ContentNegotiation) {
            json(
                Json {
                    isLenient = false
                    ignoreUnknownKeys = true
                    allowSpecialFloatingPointValues = true
                    useArrayPolymorphism = false
                }
            )
        }
    }

    suspend inline fun <T: ApiRequest, U: Parameter, reified V: Any> request(request: T, parameter: U): V {
        val response = client.request("${request.baseUrl}${request.path}") {
            method = request.method
        }
        return response.body()
    }
}

interface ApiRequest {
    val baseUrl: String
    val path: String
    val method: HttpMethod
}

internal interface Parameter {}

