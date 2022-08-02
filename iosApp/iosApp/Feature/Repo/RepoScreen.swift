//
//  RepoView.swift
//  iosApp
//
//  Created by 菅原勝也 on 2022/08/02.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared
import Combine

struct RepoScreen: View {
    @StateObject private var proxy = RepoViewProxy()
    @StateObject private var holder: ControllerHolder

    init(storeFactory: StoreFactory) {
        _holder = StateObject(wrappedValue: ControllerHolder { lifecycle in
            RepoController(
                lifecycle: lifecycle,
                instanceKeeper: InstanceKeeperDispatcherKt.InstanceKeeperDispatcher(),
                storeFactory: storeFactory
            )
        })
    }

    var body: some View {
        List {
            Button("Fetch") {
                proxy.dispatch(event: RepoViewEvent.FetchClicked())
            }
            ForEach(proxy.model.repos, id: \.id) { repo in
                Text(repo.name)
            }
        }
        .onFirstAppear {
            holder.controller.onViewCreated(view: proxy, viewLifecycle: holder.lifecycle)
        }
        .onAppear { LifecycleRegistryExtKt.resume(holder.lifecycle) }
        .onDisappear { LifecycleRegistryExtKt.stop(holder.lifecycle) }
    }
}

private final class RepoViewProxy: BaseMviView<RepoViewModel, RepoViewEvent>, RepoView, ObservableObject {

    @Published var model = RepoViewModel()

    override func render(model: RepoViewModel) {
        self.model = model
    }
}

private final class ControllerHolder: ObservableObject {
    let lifecycle: LifecycleRegistry = LifecycleRegistryKt.LifecycleRegistry()
    var controller: RepoController

    init(factory: (Lifecycle) -> RepoController) {
        controller = factory(lifecycle)
    }

    deinit {
        LifecycleRegistryExtKt.destroy(lifecycle)
    }
}
