import SwiftUI
import Resolver
import FirebaseCore

struct ContentView: View {
    @InjectedObject var authViewModel: AuthenticationViewModel
    @InjectedObject var deepLinkingViewModel: DeepLinkingViewModel

	var body: some View {
        ZStack {
            VStack(alignment: .leading, spacing: 0) {
                NetworkMonitorIndicator()

                if authViewModel.isAuthenticated == true {
                    TabView()
                        .background(Color("Background"))
                        .ignoresSafeArea(edges: .all)
                        .onOpenURL { url in
                            deepLinkingViewModel.checkDeepLink(url: url)
                        }
                } else if authViewModel.isAuthenticated == false {
                    OnBoarding()
                } else {
                    CustomLoader()
                }
            }

            UpdateView()
        }
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
