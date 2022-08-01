package net.listadoko.myfirstkmm2

import com.arkivanov.mvikotlin.core.view.BaseMviView

// これを定義しないとiOS側で利用できない
abstract class MyBaseMviView<in Model : Any, Event : Any>: BaseMviView<Model, Event>() {}
