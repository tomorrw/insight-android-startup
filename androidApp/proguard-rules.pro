-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt
-keep,includedescriptorclasses class com.tomorrow.lda.shared.**$$serializer { *; }
-keepclassmembers class com.tomorrow.lda.shared.** {
    *** Companion;
}
-keepclasseswithmembers class com.tomorrow.lda.shared.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep class com.tomorrow.lda.feature_navigation.**
-keep,includedescriptorclasses class com.tomorrow.lda.feature_navigation** { *; }
-keepclassmembers class com.tomorrow.lda.feature_navigation.** {
    *** Companion;
}
-keep class com.tomorrow.lda.feature_navigation.**
