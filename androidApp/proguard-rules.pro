-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt
-keep,includedescriptorclasses class com.tomorrow.convenire.shared.**$$serializer { *; }
-keepclassmembers class com.tomorrow.convenire.shared.** {
    *** Companion;
}
-keepclasseswithmembers class com.tomorrow.convenire.shared.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep class com.tomorrow.convenire.feature_navigation.**
-keep,includedescriptorclasses class com.tomorrow.convenire.feature_navigation** { *; }
-keepclassmembers class com.tomorrow.convenire.feature_navigation.** {
    *** Companion;
}
-keep class com.tomorrow.convenire.feature_navigation.**
