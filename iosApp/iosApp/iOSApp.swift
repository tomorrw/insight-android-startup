import SwiftUI
import shared
import UiComponents
import ios_project_startup

@main
struct iOSApp: App {
    // register app delegate for Firebase setup
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
        @AppStorage("selectedColorTheme") private var selectedColorTheme = "Auto"
    
    func decideTheme() -> UIUserInterfaceStyle {
        switch selectedColorTheme {
        case "Auto":
            return .unspecified
        case "Light":
            return .light
        case "Dark":
            return .dark
        default:
            return .unspecified
        }
    }
    
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
                .defaultTheme(
                    colorTheme: decideTheme(),
                    themeColor: ThemeColor(
                        foreground: Color("Primary"),
                        background: Color("Background"),
                        tint: Color("Primary"),
                        accentColor: Color("Background")
                    ))
        }
    }
}



