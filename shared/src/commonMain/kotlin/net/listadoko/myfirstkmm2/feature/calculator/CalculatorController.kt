package net.listadoko.myfirstkmm2.feature.calculator

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.binder.Binder
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.extensions.coroutines.events
import kotlinx.coroutines.flow.map

class CalculatorController(lifecycle: Lifecycle,
                           instanceKeeper: InstanceKeeper,
                           private val storeFactory: StoreFactory)
{
    private val store = instanceKeeper.getStore {
        CalculatorStoreFactory(storeFactory).create()
    }
    private var binder: Binder? = null
    private val statesToModel: CalculatorStore.State.() -> CalculatorView.Model =
        {
            CalculatorView.Model(
                value = value.toString()
            )
        }
    private val eventToIntent: CalculatorView.Event.() -> CalculatorStore.Intent =
        {
            when (this) {
                is CalculatorView.Event.IncrementClicked -> CalculatorStore.Intent.Increment
                is CalculatorView.Event.DecrementClicked -> CalculatorStore.Intent.Decrement
                is CalculatorView.Event.SumClicked -> CalculatorStore.Intent.Sum(this.n)
            }
        }

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
