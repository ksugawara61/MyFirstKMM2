package net.listadoko.myfirstkmm2.repository.api

import io.ktor.http.*
import kotlinx.serialization.Serializable
import net.listadoko.myfirstkmm2.model.GithubRepo

internal data class GetGithubRepoRequest(
    override val baseUrl: String = "https://api.github.com",
    override val path: String,
    override val method: HttpMethod = HttpMethod.Get,
): ApiRequest {
    constructor(userName: String) : this(path = "/users/${userName}/repos")
}

internal data class GithubRepoParameter(
    val page: Int,
    val perPage: Int
): Parameter

@Serializable
internal data class GithubRepoResponse(
    val id: Int,
    val name: String
) {
    fun to(): GithubRepo {
        return GithubRepo(id, name)
    }
}
