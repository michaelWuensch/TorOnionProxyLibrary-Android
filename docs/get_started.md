Get Started
===

## Implementation (topl-service)
 - In your App Module's `build.gradle` file, add the following to the `dependencies` block"
     ```groovy
     def topl_android_version = "{{ topl_android.release }}"
     implementation 'io.matthewnelson.topl-android:topl-core-base:$topl_android_version'
     implementation 'io.matthewnelson.topl-android:topl-service:$topl_android_version'
     ```
   
 - TODO: geoip files and binaries
 
 - Create a new class which extends [TorSettings](./topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md)
   and apply your own default settings.
   
 - In your Application class' `onCreate` implement, and customize as desired, the
   [TorServiceController.Builder](./topl-service/io.matthewnelson.topl_service/-tor-service-controller/-builder/index.md)
   
 - Call APIs provided from
   [TorServiceController.Companion](./topl-service/io.matthewnelson.topl_service/-tor-service-controller/index.md)
 
## Using the SNAPSHOT version

 - In your Project's `build.gradle` file, add the following to the `repositories` block:
     ```groovy
     mavenCentral()
     maven {
         url 'https://oss.sonatype.org/content/repositories/snapshots/'
     }
     ```
   
 - In your App module's `build.gradle` file, add (or modify) the following in the `dependencies` block:
     ```groovy
     def topl_android_version = "{{ topl_android.next_release }}-SNAPSHOT"
     implementation 'io.matthewnelson.topl-android:topl-core-base:$topl_android_version'
     implementation 'io.matthewnelson.topl-android:topl-service:$topl_android_version'
     ```

     !!! Warning
        SNAPSHOT versions are ever changing and may contain not yet fully fleshed out features. Do **not** ship a release.
