// LANGUAGE: +MultiPlatformProjects
// IGNORE_BACKEND_K1: JVM, JVM_IR, JS, JS_IR, JS_IR_ES6, WASM
// IGNORE_NATIVE_K1: mode=ONE_STAGE_MULTI_MODULE
// IGNORE_REASON: multimodule MPP tests are not supported in K1
// ISSUE: KT-59613

// MODULE: common
// FILE: common.kt
expect fun printJson(pretty: Boolean = false)
expect fun printJson(replacer: () -> Any?)

// MODULE: platform()()(common)
// FILE: platform.kt
actual fun printJson(replacer: () -> Any?) {}
actual fun printJson(pretty: Boolean) {}

fun box(): String {
    printJson()
    return "OK"
}
