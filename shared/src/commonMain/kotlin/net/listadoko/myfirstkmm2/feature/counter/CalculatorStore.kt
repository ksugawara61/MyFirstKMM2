package net.listadoko.myfirstkmm2.feature.counter

import com.arkivanov.mvikotlin.core.store.Store
import net.listadoko.myfirstkmm2.feature.counter.CalculatorStore.Intent
import net.listadoko.myfirstkmm2.feature.counter.CalculatorStore.State

internal interface CalculatorStore : Store<Intent, State, Nothing> {
    sealed interface Intent {
        object Increment : Intent
        object Decrement : Intent
        data class Sum(val n: Int): Intent
    }

    data class State(
        val value: Long = 0L
    )
}
