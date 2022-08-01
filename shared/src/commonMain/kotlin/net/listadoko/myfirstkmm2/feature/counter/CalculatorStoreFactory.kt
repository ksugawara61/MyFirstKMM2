package net.listadoko.myfirstkmm2.feature.counter

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveBootstrapper
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.scheduler.computationScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.observeOn
import com.badoo.reaktive.single.singleFromFunction
import com.badoo.reaktive.single.subscribeOn
import net.listadoko.myfirstkmm2.feature.counter.CalculatorStore.Intent
import net.listadoko.myfirstkmm2.feature.counter.CalculatorStore.State

internal class CalculatorStoreFactory(private val storeFactory: StoreFactory) {
    fun create(): CalculatorStore =
        object : CalculatorStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = "CounterStore",
            initialState = State(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        class SetValue(val value: Long): Action
    }

    private sealed interface Msg {
        class Value(val value: Long) : Msg
    }

    private inner class BootstrapperImpl : ReaktiveBootstrapper<Action>() {
        override fun invoke() {
            singleFromFunction { (1L..20.toLong()).sum() }
                .subscribeOn(computationScheduler)
                .observeOn(mainScheduler)
                .subscribeScoped { dispatch(Action.SetValue(it)) }
        }
    }

    private inner class ExecutorImpl : ReaktiveExecutor<Intent, Action, State, Msg, Nothing>() {
        override fun executeAction(action: Action, getState: () -> State) =
            when (action) {
                is Action.SetValue-> dispatch((Msg.Value(action.value)))
            }

        override fun executeIntent(intent: Intent, getState: () -> State) =
            when (intent) {
                is Intent.Increment -> dispatch(Msg.Value(getState().value + 1))
                is Intent.Decrement -> dispatch(Msg.Value(getState().value - 1))
                is Intent.Sum -> sum(intent.n)
            }

        private fun sum(n: Int) {
            singleFromFunction { (1L..n.toLong()).sum() }
                .subscribeOn(computationScheduler)
                .observeOn(mainScheduler)
                .subscribeScoped { dispatch((Msg.Value(it))) }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.Value -> copy(value = msg.value)
            }
    }
}
