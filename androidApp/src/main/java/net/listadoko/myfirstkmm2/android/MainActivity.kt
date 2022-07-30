package net.listadoko.myfirstkmm2.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import net.listadoko.myfirstkmm2.Greeting

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val greeting = findViewById<ComposeView>(R.id.greeting)
        greeting.setContent {
            GreetingText(greet())
        }
    }

    private fun greet(): String {
        return Greeting().greeting()
    }
}

@Composable
private fun GreetingText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.h5,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.margin_small))
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}
