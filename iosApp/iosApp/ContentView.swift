import UIKit
import FamilyControls
import SwiftUI
import ComposeApp
import DeviceActivity
import ScreenTimeReportExtension


struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        return MainViewControllerKt.MainViewController(
            nativeViewFactory: IOSNativeViewFactory.shared
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
       ComposeView()
           .ignoresSafeArea()

        
//        NavigationView {
//            VStack {
//                DeviceActivityReport(
//                    DeviceActivityReport.Context.socialMediaGuilt,
//                    filter: filter
//                )
//                .onAppear(perform: {
//                    let shared = UserDefaults.standard
//                    print("appeared defaults here: ", shared.integer(forKey: "demo"))
//                })
//                .background(Color.black)
//            }
//            .navigationTitle("Your Doom Awaits")
//        }.task {
//            print("ios: requestPermission")
//            Task {
//                do {
//                    try await AuthorizationCenter.shared.requestAuthorization(for: .individual)
//                    print("ios: success")
//                } catch let error {
//                    await MainActor.run {
//                        print("ios: error occured: ", error.localizedDescription)
//                        
//                    }
//                }
//            }
//        }
    }
}

class IOSNativeViewFactory: NativeViewFactory {
    func createOnboardingUsageScreen() -> UIViewController {
        let calendar = Calendar.current
        let now = Date()
        let sevenDaysAgo = calendar.date(
            byAdding: .day,
            value: -7,
            to: calendar.startOfDay(for: now)
        )!

        // End = now
        let interval = DateInterval(start: sevenDaysAgo, end: now)

        // Filter for LAST 7 DAYS
        let filter = DeviceActivityFilter(
            segment: .daily(during: interval),
            users: .all,
            devices: .init([.iPhone])
        )
        let report = DeviceActivityReport(
            DeviceActivityReport.Context.socialMediaGuilt,
            filter: filter
        ).frame(maxWidth: .infinity, maxHeight: .infinity)
        return UIHostingController(rootView: report)
    }
    
    static var shared = IOSNativeViewFactory()
    func createNativeScreen() -> UIViewController {
        let calendar = Calendar.current
        let now = Date()
        let startOfDay = calendar.startOfDay(for: now)
        let filter = DeviceActivityFilter(
            segment: .daily(
                during: DateInterval(
                    start: startOfDay,
                    end: now
                )
            ),
            users: .all,
            devices: .all
        )
        let report = DeviceActivityReport(
            DeviceActivityReport.Context.socialMediaGuilt,
            filter: filter
        ).frame(width: 500, height: 500)
        return UIHostingController(rootView: report)
    }
}




