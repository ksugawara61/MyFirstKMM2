package net.listadoko.myfirstkmm2.android.feature.repo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.essenty.instancekeeper.instanceKeeper
import com.arkivanov.essenty.lifecycle.essentyLifecycle
import com.arkivanov.mvikotlin.core.view.BaseMviView
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import net.listadoko.myfirstkmm2.android.ui.theme.MyFirstKMM2Theme
import net.listadoko.myfirstkmm2.feature.repo.RepoController
import net.listadoko.myfirstkmm2.feature.repo.RepoView
import net.listadoko.myfirstkmm2.repository.GithubRepositoryImpl

class RepoActivity : ComponentActivity() {
    private lateinit var proxy: RepoViewImpl
    private lateinit var controller: RepoController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUp()
        setContent {
            MyFirstKMM2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Body()
                }
            }
        }
    }

    private fun setUp() {
        val lifecycle = essentyLifecycle()
        proxy = RepoViewImpl()
        controller = RepoController(
            storeFactory = LoggingStoreFactory(delegate = DefaultStoreFactory()),
            instanceKeeper = instanceKeeper(),
            lifecycle = lifecycle,
            repository = GithubRepositoryImpl
        )
        controller.onViewCreated(proxy, lifecycle)
    }

    @Composable
    private fun Body() {
        val state = rememberScrollState()
        val model by proxy.model.asStateFlow().collectAsState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Button(onClick = { proxy.dispatch((RepoView.Event.FetchClicked)) }) {
                Text("Fetch")
            }
            model.repos.forEach { repo ->
                Text(
                    repo.name,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

private class RepoViewImpl: BaseMviView<RepoView.Model, RepoView.Event>(),
    RepoView {
    val model = MutableStateFlow(RepoView.Model())

    override fun render(model: RepoView.Model) {
        this.model.update { it.copy(model.repos) }
    }
}

