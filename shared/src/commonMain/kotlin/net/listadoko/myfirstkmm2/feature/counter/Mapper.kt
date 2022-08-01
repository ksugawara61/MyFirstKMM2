package net.listadoko.myfirstkmm2.feature.counter

internal val statesToModel: CalculatorStore.State.() -> CalculatorView.Model =
    {
        CalculatorView.Model(
            value = value.toString()
        )
    }

internal val eventToIntent: CalculatorView.Event.() -> CalculatorStore.Intent =
    {
        when (this) {
            is CalculatorView.Event.IncrementClicked -> CalculatorStore.Intent.Increment
            is CalculatorView.Event.DecrementClicked -> CalculatorStore.Intent.Decrement
        }
    }
