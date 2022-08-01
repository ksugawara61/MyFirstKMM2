import SwiftUI
import shared
import Combine

class CalculatorViewProxy: MyBaseMviView<CalculatorViewModel, CalculatorViewEvent>, CalculatorView, ObservableObject {

    @Published var model = CalculatorViewModel()

    override func render(model: CalculatorViewModel) {
        self.model = model
    }
}

struct ContentView: View {
    @ObservedObject var proxy = CalculatorViewProxy()
	let greet = Greeting().greeting()

	var body: some View {
        VStack {
            Text(proxy.model.value)
            Button(action: { proxy.dispatch(event: CalculatorViewEvent.IncrementClicked()) }) {
                Text("Increment")
            }
            Button(action: { proxy.dispatch(event: CalculatorViewEvent.IncrementClicked()) }) {
                Text("Decrement")
            }
        }
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
