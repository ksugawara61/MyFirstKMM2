package net.listadoko.myfirstkmm2.feature.calculator

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.listadoko.myfirstkmm2.feature.calculator.CalculatorStore.Intent
import net.listadoko.myfirstkmm2.feature.calculator.CalculatorStore.State

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

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                val sum = withContext(Dispatchers.Default) { (1L..20.toLong()).sum() }
                dispatch(Action.SetValue(sum))
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Nothing>() {
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
            scope.launch {
                val sum = withContext(Dispatchers.Default) { (1L..n.toLong()).sum() }
                dispatch((Msg.Value(sum)))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.Value -> copy(value = msg.value)
            }
    }
}
