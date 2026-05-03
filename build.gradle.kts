// Top-level build file
plugins {
    alias(libs.plugins.android.application) apply false
    // Baris 'library' dihapus karena lo nggak pakai library module terpisah
    id("org.jetbrains.kotlin.android") version "1.9.21" apply false
}