package com.honeycomb.disciplineapp.presentation.focus_app

import kotlinx.coroutines.suspendCancellableCoroutine
import com.honeycomb.screentime.ScreenTimeManagerSwift
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
actual class ScreenTimeManager {
    private val manager = ScreenTimeManagerSwift()

    actual suspend fun requestPermission(): Boolean = suspendCancellableCoroutine { cont ->
        println("starting requestPermission")
        manager.requestPermissionWithCompletion {
            println("got a callback: $it")
        }
    }

    actual suspend fun startFocusSession(minutes: Int) {
        // No-op: blocking happens on app select
    }

    actual suspend fun getBlockableApps(): List<AppInfo> {
       return emptyList()
    }

    actual suspend fun blockApps(appIds: List<String>, minutes: Int) {

    }

    actual suspend fun stopFocusSession() {

    }
}

// These must match Swift method names exactly
//
//@OptIn(ExperimentalForeignApi::class)
//private external fun getScreenTimeManager(): COpaquePointer?
//
//@OptIn(ExperimentalForeignApi::class)
//private fun COpaquePointer.requestPermissionWithCompletion(completion: CPointer<CFunction<(Boolean) -> Unit>>) =
//    objc_msgSend(sel_getUid("requestPermissionWithCompletion:"), completion)
//
//@OptIn(ExperimentalForeignApi::class)
//private fun COpaquePointer.startMonitoringWithBlockedApps_minutes(apps: Any, minutes: Int) =
//    objc_msgSend(this, sel_getUid("startMonitoringWithBlockedApps:minutes:"), apps, minutes)
//
//@OptIn(ExperimentalForeignApi::class)
//private fun COpaquePointer.stopMonitoring() =
//    objc_msgSend(this, sel_getUid("stopMonitoring"))
//
//actual class ScreenTimeManager {
//    @OptIn(ExperimentalForeignApi::class)
//    private val swift = getScreenTimeManager()!!
//
//    actual suspend fun requestPermission(): Boolean = suspendCancellableCoroutine { cont ->
//        val block: (Boolean) -> Unit = { success ->
//            cont.resume(success)
//        }
//        val objcBlock = blockLiteral(block)
//
//        swift.requestPermissionWithCompletion(objcBlock)
//    }
//
//    actual suspend fun blockApps(appIds: List<String>, minutes: Int) {
//        val swiftApps = swift.applicationsFromBundleIds(appIds.toNSArray())
//        swift.startMonitoringWithBlockedApps_minutes(swiftApps, minutes)
//    }
//
//    actual suspend fun stopFocusSession() {
//        swift.stopMonitoring()
//    }
//
//    actual suspend fun getBlockableApps(): List<AppInfo> = listOf(
//        AppInfo("com.instagram.Instagram", "Instagram"),
//        AppInfo("com.tiktok.TikTok", "TikTok"),
//        AppInfo("com.facebook.Facebook", "Facebook"),
//        AppInfo("com.snapchat.Snapchat", "Snapchat"),
//        AppInfo("com.google.youtube", "YouTube"),
//        AppInfo("com.twitter.Twitter", "X")
//    )
//
//    actual suspend fun startFocusSession(minutes: Int) { /* no-op */ }
//}
//
//// Helper extensions
//private inline fun <reified T> T.blockLiteral(crossinline block: (T) -> Unit): CPointer<CFunction<(COpaquePointer?, T) -> Unit>> {
//    return staticCFunction { _: COpaquePointer?, value: T -> block(value) }
//}
//
//private fun List<String>.toNSArray(): NSArray = this.toNSArray()