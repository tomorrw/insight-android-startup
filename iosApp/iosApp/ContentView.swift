import SwiftUI
import Resolver
import FirebaseCore
import UiComponents
import ConnectivityUtils
import AppStoreFeatures
import shared

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
            
            UpdateView(vm: UpdateViewModel(
                getUpdateType: updateType,
                fetchAppStoreInfo: fetchAppStoreInfo
            ))
        }
    }
    
    private func fetchAppStoreInfo() -> AppInfo {
        let appConfig = GetAppConfigUseCase().get()
        return AppInfo(
            name: appConfig.name,
            updateUrl: URL(string: appConfig.updateUrl ?? "tomorrow.services")
        )
    }
    
    private func updateType() async throws -> UpdateViewModel.UpdateType {
        let result = try await GetUpdateTypeUseCase().getType()
        switch result {
        case .flexible:
            return UpdateViewModel.UpdateType.flexible
        case .forced:
            return UpdateViewModel.UpdateType.forced
        default:
            return UpdateViewModel.UpdateType.none
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
