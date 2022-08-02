package net.listadoko.myfirstkmm2.feature.repo

import com.arkivanov.mvikotlin.core.store.Store
import net.listadoko.myfirstkmm2.repository.api.GithubRepoResponse

internal interface RepoStore : Store<RepoStore.Intent, RepoStore.State, Nothing> {
    sealed interface Intent {
        object Fetch: Intent
    }

    data class State(
        val repos: List<GithubRepoResponse> = emptyList()
    )
}
