package net.listadoko.myfirstkmm2.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import com.arkivanov.essenty.instancekeeper.instanceKeeper
import com.arkivanov.essenty.lifecycle.essentyLifecycle
import com.arkivanov.mvikotlin.core.view.BaseMviView
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import net.listadoko.myfirstkmm2.feature.calculator.CalculatorController
import net.listadoko.myfirstkmm2.feature.calculator.CalculatorView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainActivity : AppCompatActivity() {
    private lateinit var proxy: CalculatorViewImpl
    private lateinit var controller: CalculatorController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lifecycle = essentyLifecycle()
        proxy = CalculatorViewImpl()
        controller = CalculatorController(
            storeFactory = LoggingStoreFactory(delegate = DefaultStoreFactory()),
            instanceKeeper = instanceKeeper(),
            lifecycle = lifecycle
        )
        controller.onViewCreated(proxy, lifecycle)

        setContent {
            Body()
        }
    }

    @Composable
    private fun Body() {
        val model by proxy.model.asStateFlow().collectAsState()
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = AnnotatedString(model.value)
            )
            Button(onClick = { proxy.dispatch(CalculatorView.Event.IncrementClicked) }) {
                Text("Increment")
            }
            Button(onClick = { proxy.dispatch(CalculatorView.Event.DecrementClicked) }) {
                Text("Decrement")
            }
            Button(onClick = { proxy.dispatch(CalculatorView.Event.SumClicked(n = 10)) }) {
                Text("Sum")
            }
        }
    }
}

private class CalculatorViewImpl: BaseMviView<CalculatorView.Model, CalculatorView.Event>(), CalculatorView {
    val model = MutableStateFlow(CalculatorView.Model())

    override fun render(model: CalculatorView.Model) {
        this.model.update { it.copy(model.value) }
    }
}
