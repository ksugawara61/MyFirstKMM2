package net.listadoko.myfirstkmm2.feature.repo

import com.arkivanov.mvikotlin.core.view.MviView
import net.listadoko.myfirstkmm2.repository.api.GithubRepoResponse

interface RepoView : MviView<RepoView.Model, RepoView.Event> {
    data class Model(
        val repos: List<GithubRepoResponse>
    ) {
        constructor() : this(
            repos = emptyList()
        )
    }

    sealed class Event {
        object FetchClicked: Event()
    }
}
