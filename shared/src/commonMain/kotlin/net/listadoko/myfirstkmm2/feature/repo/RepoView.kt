package net.listadoko.myfirstkmm2.feature.repo

import com.arkivanov.mvikotlin.core.view.MviView
import net.listadoko.myfirstkmm2.model.GithubRepo

interface RepoView : MviView<RepoView.Model, RepoView.Event> {
    data class Model(
        val repos: List<GithubRepo>
    ) {
        constructor() : this(
            repos = emptyList()
        )
    }

    sealed class Event {
        object FetchClicked: Event()
    }
}
