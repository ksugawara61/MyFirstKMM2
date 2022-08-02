package net.listadoko.myfirstkmm2.feature.repo

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.listadoko.myfirstkmm2.model.GithubRepo
import net.listadoko.myfirstkmm2.repository.GithubRepository

internal class RepoStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: GithubRepository
) {
    fun create(): RepoStore =
        object :RepoStore, Store<RepoStore.Intent, RepoStore.State, Nothing> by storeFactory.create(
            name = "RepoStore",
            initialState = RepoStore.State(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        class Repos(val repos: List<GithubRepo>): Msg
    }

    private inner class ExecutorImpl : CoroutineExecutor<RepoStore.Intent, Nothing, RepoStore.State, Msg, Nothing>() {
        override fun executeIntent(intent: RepoStore.Intent, getState: () -> RepoStore.State) {
            when (intent) {
                is RepoStore.Intent.Fetch -> fetch()
            }
        }

        private fun fetch() {
            scope.launch {
                val response = withContext(Dispatchers.Default) { repository.fetchRepositories() }
                dispatch(Msg.Repos(response))
            }
        }
    }

    private object ReducerImpl : Reducer<RepoStore.State, Msg> {
        override fun RepoStore.State.reduce(msg: Msg): RepoStore.State =
            when (msg) {
                is Msg.Repos -> copy(repos = msg.repos)
            }
    }
}
