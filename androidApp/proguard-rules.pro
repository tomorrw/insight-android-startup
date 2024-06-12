-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt
-keep,includedescriptorclasses class com.tomorrow.mobile_starter_app
.shared.**$$serializer { *; }
-keepclassmembers class com.tomorrow.mobile_starter_app
.shared.** {
    *** Companion;
}
-keepclasseswithmembers class com.tomorrow.mobile_starter_app
.shared.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep class com.tomorrow.mobile_starter_app
.feature_navigation.**
-keep,includedescriptorclasses class com.tomorrow.mobile_starter_app
.feature_navigation** { *; }
-keepclassmembers class com.tomorrow.mobile_starter_app
.feature_navigation.** {
    *** Companion;
}
-keep class com.tomorrow.mobile_starter_app
.feature_navigation.**

-keep class com.tomorrow.mobile_starter_app
.shared.data.data_source.local.**
-keep,includedescriptorclasses class com.tomorrow.mobile_starter_app
.shared.data.data_source.local.** { *; }
-keepclassmembers class com.tomorrow.mobile_starter_app
.shared.data.data_source.local.** {
    *** Companion;
}
-keep class com.tomorrow.mobile_starter_app
.shared.data.data_source.local.**

-keep interface io.realm.kotlin.types.** { *; }
-keep class io.realm.kotlin.types.** { *; }
-keep class io.realm.kotlin.types.**
-keep @io.realm.kotlin.types.** class *
-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class *
-dontwarn org.slf4j.impl.StaticLoggerBinder