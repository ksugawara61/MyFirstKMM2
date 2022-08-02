package net.listadoko.myfirstkmm2.repository.api

import io.ktor.http.*
import kotlinx.serialization.Serializable

data class GetGithubRepoRequest(
    override val baseUrl: String = "https://api.github.com",
    override val path: String,
    override val method: HttpMethod = HttpMethod.Get,
): ApiRequest {
    constructor(userName: String) : this(path = "/users/${userName}/repos")
}

data class GithubRepoParameter(
    val page: Int,
    val perPage: Int
): Parameter

@Serializable
data class GithubRepoResponse(
    val id: Int,
    val name: String
)

object Sample {
    suspend fun request(): List<GithubRepoResponse> {
        return ApiClient.request(
            request = GetGithubRepoRequest("ksugawara61"),
            parameter = GithubRepoParameter(page = 1, perPage = 5)
        )
    }
}
