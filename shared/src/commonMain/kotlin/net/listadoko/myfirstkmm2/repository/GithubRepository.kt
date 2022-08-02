package net.listadoko.myfirstkmm2.repository

import net.listadoko.myfirstkmm2.model.GithubRepo
import net.listadoko.myfirstkmm2.repository.api.ApiClient
import net.listadoko.myfirstkmm2.repository.api.GetGithubRepoRequest
import net.listadoko.myfirstkmm2.repository.api.GithubRepoParameter
import net.listadoko.myfirstkmm2.repository.api.GithubRepoResponse

interface GithubRepository {
    suspend fun fetchRepositories(): List<GithubRepo>
}

object GithubRepositoryImpl: GithubRepository {
    override suspend fun fetchRepositories(): List<GithubRepo> {
        return ApiClient.request<GetGithubRepoRequest, GithubRepoParameter, List<GithubRepoResponse>>(
            request = GetGithubRepoRequest("ksugawara61"),
            parameter = GithubRepoParameter(page = 1, perPage = 5)
        ).map { it.to() }
    }
}
