import SwiftUI
import ComposeApp
import Firebase
import GoogleSignIn
import DeviceActivity

@main
struct iOSApp: App {
    init() {
        KoinSetupKt.doInitKoin()
        FirebaseApp.configure()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

class AppDelegate: NSObject, UIApplicationDelegate {

    func application(
      _ app: UIApplication,
      open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]
    ) -> Bool {
      var handled: Bool

    // Let Google Sign-In handle the URL if it's related to Google Sign-In
      handled = GIDSignIn.sharedInstance.handle(url)
      if handled {
        return true
      }

      // Handle other custom URL types.

      // If not handled by this app, return false.
      return false
    }
}
