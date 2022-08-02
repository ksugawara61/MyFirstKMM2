import SwiftUI
import shared
import Combine

struct CounterView: View {
    @StateObject private var proxy = CalculatorViewProxy()
    @StateObject private var holder: ControllerHolder

    @State var greet = "Loading..."

    init(storeFactory: StoreFactory) {
        _holder = StateObject(wrappedValue: ControllerHolder { lifecycle in
            CalculatorController(
                lifecycle: lifecycle,
                instanceKeeper: InstanceKeeperDispatcherKt.InstanceKeeperDispatcher(),
                storeFactory: storeFactory
            )
        })
    }

	var body: some View {
        VStack {
            Text(proxy.model.value)
            Button(action: { proxy.dispatch(event: CalculatorViewEvent.IncrementClicked()) }) {
                Text("Increment")
            }
            Button(action: { proxy.dispatch(event: CalculatorViewEvent.DecrementClicked()) }) {
                Text("Decrement")
            }
            Button(action: { proxy.dispatch(event: CalculatorViewEvent.SumClicked(n: 10)) }) {
                Text("Sum")
            }
            Text(greet).onAppear { load() }
        }
        .onFirstAppear {
            holder.controller.onViewCreated(view: proxy, viewLifecycle: holder.lifecycle)
        }
        .onAppear { LifecycleRegistryExtKt.resume(holder.lifecycle) }
        .onDisappear { LifecycleRegistryExtKt.stop(holder.lifecycle) }
	}

    private func load() {
        Sample.shared.request(completionHandler: { result, error in
            guard let result = result else { return }
            print(result)
        })
    }
}

private final class CalculatorViewProxy: BaseMviView<CalculatorViewModel, CalculatorViewEvent>, CalculatorView, ObservableObject {

    @Published var model = CalculatorViewModel()

    override func render(model: CalculatorViewModel) {
        self.model = model
    }
}

private final class ControllerHolder: ObservableObject {
    let lifecycle: LifecycleRegistry = LifecycleRegistryKt.LifecycleRegistry()
    var controller: CalculatorController

    init(factory: (Lifecycle) -> CalculatorController) {
        controller = factory(lifecycle)
    }

    deinit {
        LifecycleRegistryExtKt.destroy(lifecycle)
    }
}
