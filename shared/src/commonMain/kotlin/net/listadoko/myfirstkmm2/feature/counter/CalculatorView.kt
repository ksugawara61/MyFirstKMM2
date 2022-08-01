package net.listadoko.myfirstkmm2.feature.counter

import com.arkivanov.mvikotlin.core.view.MviView
import net.listadoko.myfirstkmm2.feature.counter.CalculatorView.Model
import net.listadoko.myfirstkmm2.feature.counter.CalculatorView.Event

interface CalculatorView : MviView<Model, Event> {
    data class Model(
        val value: String
    ) {
        constructor() : this(
            value = "0"
        )
    }

    sealed class Event {
        object IncrementClicked: Event()
        object DecrementClicked: Event()
        data class SumClicked(val n: Int): Event()
    }
}
