package net.listadoko.myfirstkmm2.feature.repo

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.binder.Binder
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.events
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.map
import net.listadoko.myfirstkmm2.repository.GithubRepository

class RepoController(
    lifecycle: Lifecycle,
    instanceKeeper: InstanceKeeper,
    private val storeFactory: StoreFactory,
    private val repository: GithubRepository
) {
    private val store = instanceKeeper.getStore {
        RepoStoreFactory(storeFactory, repository).create()
    }
    private var binder: Binder? = null
    private val statesToModel: RepoStore.State.() -> RepoView.Model =
        {
            RepoView.Model(
                repos = repos
            )
        }
    private val eventToIntent: RepoView.Event.() -> RepoStore.Intent =
        {
            when (this) {
                is RepoView.Event.FetchClicked -> RepoStore.Intent.Fetch
            }
        }

    init {
        lifecycle.doOnDestroy(store::dispose)
    }

    fun onViewCreated(view: RepoView, viewLifecycle: Lifecycle) {
        binder = bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
            store.states.map(statesToModel) bindTo view
            view.events.map(eventToIntent) bindTo store
        }
    }
}
