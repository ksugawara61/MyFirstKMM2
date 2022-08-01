package net.listadoko.myfirstkmm2.feature.counter

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.binder.Binder
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.bind
import com.arkivanov.mvikotlin.extensions.reaktive.events
import com.arkivanov.mvikotlin.extensions.reaktive.states
import com.badoo.reaktive.observable.map

class CalculatorController(lifecycle: Lifecycle,
                           instanceKeeper: InstanceKeeper,
                           private val storeFactory: StoreFactory)
{
    private val store = instanceKeeper.getStore {
        CalculatorStoreFactory(storeFactory).create()
    }

    private var binder: Binder? = null

    init {
        lifecycle.doOnDestroy(store::dispose)
    }

    fun onViewCreated(view: CalculatorView, viewLifecycle: Lifecycle) {
        binder = bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
            store.states.map(statesToModel) bindTo view
            view.events.map(eventToIntent) bindTo store
        }
    }
}
