# Aturan umum untuk Android Release
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Retrofit & OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# Gson (agar data class API dan Entity tidak teracak oleh ProGuard)
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep class com.financeapp.data.remote.** { *; }
-keep class com.financeapp.domain.model.** { *; }

# Room Database
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# ML Kit Text Recognition
-keep class com.google.mlkit.** { *; }
-dontwarn com.google.mlkit.**

# SQLCipher
-keep class net.zetetic.database.sqlcipher.** { *; }
-dontwarn net.zetetic.database.sqlcipher.**