import SwiftUI
import shared
import Combine

@main
struct iOSApp: App {
    @StateObject private var holder = Holder()

    var body: some Scene {
		WindowGroup {
			ContentView(
                storeFactory: holder.storeFactory
            )
		}
	}
}

private class Holder : ObservableObject {
    let storeFactory: StoreFactory

    init() {
        storeFactory = LoggingStoreFactory(delegate: DefaultStoreFactory())
    }
}
