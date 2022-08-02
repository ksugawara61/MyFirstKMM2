import SwiftUI
import shared
import Combine

@main
struct iOSApp: App {
    @StateObject private var holder = Holder()

    var body: some Scene {
		WindowGroup {
            TabView {
                CalculatorScreen(storeFactory: holder.storeFactory).tabItem {
                    Image(systemName: "plus.app")
                    Text("Calculator")
                }
                RepoScreen(storeFactory: holder.storeFactory).tabItem {
                    Image(systemName: "list.dash")
                    Text("Repo")
                }
            }
		}
	}
}

private class Holder : ObservableObject {
    let storeFactory: StoreFactory

    init() {
        storeFactory = LoggingStoreFactory(delegate: DefaultStoreFactory())
    }
}
