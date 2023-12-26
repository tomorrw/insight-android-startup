import SwiftUI
import shared


@main
struct iOSApp: App {
    // register app delegate for Firebase setup
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .onAppear {
                    UIView.appearance(whenContainedInInstancesOf: [UIAlertController.self]).tintColor = UIColor(named: "HighlightPrimary")
                    let scenes = UIApplication.shared.connectedScenes
                    guard let scene = scenes.first as? UIWindowScene else { return }
                    scene.keyWindow?.overrideUserInterfaceStyle = .light
                    
                    UIBarButtonItem.appearance().setTitleTextAttributes([NSAttributedString.Key.font: UIFont.systemFont(ofSize: 18.0)], for: UIControl.State.normal)
                    UIBarButtonItem.appearance().setTitleTextAttributes([NSAttributedString.Key.font: UIFont.systemFont(ofSize: 18.0)], for: .highlighted)
                    UIBarButtonItem.appearance().setTitleTextAttributes([NSAttributedString.Key.font: UIFont.systemFont(ofSize: 18.0)], for: .focused)
                    
                    UINavigationBar.appearance().titleTextAttributes = [.font : UIFont.systemFont(ofSize: 20.0)]
                    UIScrollView.appearance().keyboardDismissMode = .interactive
                }
                .defaultTheme()
        }
    }
}



